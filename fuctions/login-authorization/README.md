# process

- 关闭SpringSecurity的cookie-session验证授权，改用JWT验证授权

- token封装username，每次请求会获取token包含的username

  根据username调用实现UserDetails的AdminUserDetails的userDetailsService()，查询用户的基本信息与权限信息

- 判断权限判断是否满足