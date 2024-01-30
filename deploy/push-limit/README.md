## knowledge

- **令牌桶原理**

  > [reference](https://www.cnblogs.com/bossma/p/15659708.html)

  容量X的令牌桶，在Y单位时间内，放置Z个令牌；每Y个单位时间内，取出Z个令牌，取不到令牌则相当于限流

  

  

## bugs

- **更新qps，而令牌桶的限流限制不变**

  为该限流接口设置新的key
