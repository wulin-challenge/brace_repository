-- checkAndSetDemo.lua
-- 从redis中获取指定key的值
local current = redis.call('GET', KEYS[1])

-- 打印该key对应的值
print(current)

-- 若key对应的值不存在则current为false,否则为具体值
if current then
	print('当前'..KEYS[1]..'对应的值为: '..current)
else
	redis.call('SET', KEYS[1], ARGV[2])
  	return true
end

-- 检测当前值与预期值是否相等,相等则设置对应的值,不相等则不设置
if current == ARGV[1]
  then redis.call('SET', KEYS[1], ARGV[2])
  return true
end
return false