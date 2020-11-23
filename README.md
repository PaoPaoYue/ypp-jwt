# ypp-jwt
<a href="https://search.maven.org/artifact/com.github.paopaoyue/ypp-jwt/1.0.2/jar"><img alt="Maven Central" src="https://img.shields.io/maven-central/v/com.github.paopaoyue/ypp-jwt"></a>
<a href="https://github.com/PaoPaoYue/ypp-jwt/blob/main/LICENSE"><img alt="GitHub" src="https://img.shields.io/github/license/paopaoyue/ypp-jwt"></a>
<br>
A simple JWT authorization framework for Spring Boot applications. 

Some ideas and codes come from:
- [shrio-with-jwt-spring-boot-starter](https://github.com/davidfantasy/shrio-with-jwt-spring-boot-starter)
- [sa-token](https://github.com/click33/sa-token)

### Installation
**Gradle**
```groovy
    implementation 'com.github.paopaoyue:ypp-jwt:1.0.2'
```
**Maven**
```xml
    <dependency>
      <groupId>com.github.paopaoyue</groupId>
      <artifactId>ypp-jwt</artifactId>
      <version>1.0.2</version>
    </dependency>
```

### What can this framework do

- Use [JWT](https://jwt.io/introduction/) to secure access to the resource of your application
- Customize the payload to your needs.
- Customize the permission of different user classes.
- Use annotations to authenticate users and check their permissions.
- Refresh the token after a certain period, and expire it after a longer period
- Use Caching to speed up authentication

### How does it work

1. Define your payload - user information and user permissions
    
        public class UserAuthVo implements JwtSubject {
            private int uid;
            private String username;
        
            public UserAuthVo() {
        
            }
            public UserAuthVo(int uid, String username){
                this.uid=uid;
                this.username=username;
            }
        
            /* omit getter and setters */
        
            @Override
            public List<String> getPermissions() {
                return Collections.singletonList("user");
            }
        
            @Override
            public void setClaims(Map<String, Object> claims) {
                uid = (int) claims.get("uid");
                username = (String) claims.get("username");
            }
        
            @Override
            public Map<String, Object> getClaims() {
                Map<String, Object> map = new HashMap<>();
                map.put("uid",uid);
                map.put("username", username);
                return map;
            }
        }
 
 2. Define a service to handle CheckLogin and CheckPermission exception
 
         @Service
        public class JwtExceptionServiceImpl implements IJwtExceptionService {
        
            @Override
            public Object getLoginExceptionResponse() {
                return "not login";
            }
        
            @Override
            public Object getPermissionExceptionResponse() {
                return "no permission";
            }
        }
        
 3. Issue the token through "Assess-token" response header
 
        @PostMapping("/login")
        Map<String, String> login() {
            /*
             * do your password authentication before issue token
             */
            String token = JwtUtil.issue(new UserAuthVo(1, "ypp"));
            /*
             * you do not need to put token into response body
             * as when you call issue() it has been put into "Assess-token" response header
             */
            Map<String, String> map = new HashMap<>();
            map.put("access-token", token);
            return map;
        }
        
 4. Frontend receives the token and stores the token in localStorage
 
 5. Frontend puts this token in `Authorization` header when send a request
 
    for example:  `Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjb20ueXBwLml0cHJvamVjdC52by5TdHVkZW50QXV0aFZvIiwiZGF0YSI6eyJpZCI6MTAsInVzZXJuYW1lIjoicGFvcGFvMyJ9LCJleHAiOjE2MDA1MDkzMjB9.a__FBE8cdtMLER1DHf7bbidF2ODbY8KO19zdszOaJ9s`
 
 6. Authenticate the request using annotation
 
        // pass - if the bearer token is valid
        // otherwise throw CheckLogin exception
        @CheckLogin
        @GetMapping("/auth")
        Object auth() {
            UserAuthVo studentAuthVo = (UserAuthVo) JwtUtil.extract();
            logger.info(studentAuthVo.toString());
            return "success";
        }
    
        // not pass - the UserAuthVo has no "student" permission
        // throw CheckPermission exception
        @CheckLogin
        @CheckPermission("student")
        @GetMapping("/perm1")
        Object perm1() {
            return "success";
        }
    
        // pass - the UserAuthVo has "user" permission
        @CheckLogin
        @CheckPermission("user")
        @GetMapping("/perm2")
        Object perm2() {
            return "success";
        }
        
7. When a token should be refreshed, a new token will be automatically sent through "Assess-token" response header,
   and replace the token stored in fronend
        
### Configuration Properties
 
| Property Name | Description | Default Value |
| --- | --- | --- |
| ypp-jwt.secret| the secret key to generate signature for JWT | "1234“ |
| ypp-jwt.jsonKey| the json key of the payload object in JWT payload | "data“ |
| ypp-jwt.maxAlive| the period of time(min) before refreshing a token | 30 |
| ypp-jwt.maxIdle| the period of time(min) before expire a token | 60 |
| ypp-jwt.autoRefresh| enable refreshing tokens | true |
| ypp-jwt.cache.enable| enable caching tokens | true |
| ypp-jwt.cache.size| the maximum caching size | 1000 |
| ypp-jwt.cache.expiration| the period of time(min) before expire a token record in the cache | 30 |

### Sample Frontend Code
        
- GET and POST through Axios        
        
        function refreshToken(response) {
            let token = response.headers['access-token']
            if (token) 
                store.dispatch('login', token)
        }
        
        export default {
            post(url, params) {
                return axios({
                    headers: {
                        "Authorization" : "Bearer " + (store.getters.isLogin ? store.getters.token : '')
                    },
                    method: "post",
                    url,
                    data: params
                }).then(response => {
                    refreshToken(response)
                    return checkStatus(response)
                });
            },
            get(url, params) {
                params = qs.stringify(params)
                return axios({
                    headers: {
                        "Authorization": "Bearer " + (store.getters.isLogin ? store.getters.token : '')
                    },
                    method: "get",
                    url,
                    params
                }).then(response => {
                    refreshToken(response)
                    return checkStatus(response)
                });
            }
        };

- Decrypt the payload

        import base64 from 'base-64'
        
        function parseToken(token) {
            let res = undefined
            let sections = token.split('.')
            if (sections.length === 3) {
                let cred = base64.decode(sections[1])
                res = JSON.parse(cred)
            }
            return res ? res.data : undefined 
        }

- Store in localStorage using Vuex

        const state = {
          token: '',
          uid: 0,
          username: '',
          isLogin: false,
        }
        
        const getters = {
          token(state) {
            if (!state.token && localStorageEnabled)
              state.token = localStorage.getItem('token')
            return state.token
          },
          uid(state) {
            if (!state.uid && localStorageEnabled)
              state.uid = localStorage.getItem('uid')
            return state.uid
          },
          username(state) {
            if (!state.username && localStorageEnabled)
              state.username = localStorage.getItem('username')
            return state.username
          },
          isLogin(state) {
            if (!state.token && localStorageEnabled)
              state.token = localStorage.getItem('token')
            return state.token && state.token !== ''
          }
        }
        
        const mutations = {
          login(state, token) {
            let res = parseToken(token)
            if (res) {
              localStorage.setItem('token', token)
              localStorage.setItem('uid', res.uid)
              localStorage.setItem('username', res.username)
              state.token = token
              state.uid = res.uid
              state.username = res.username
              state.isLogin = true
            }
          },
          logout(state) {
            localStorage.removeItem('token')
            localStorage.removeItem('uid')
            localStorage.removeItem('username')
            state.token = ''
            state.uid = 0
            state.username = ''
            state.isLogin = false
          }
        }
        
        const actions = {
          login(context, token) {
            context.commit('login', token)
          },
          logout(context) {
            context.commit('logout')
          }
        }
        
        export default new Vuex.Store({
          state,
          getters,
          mutations,
          actions
        })
