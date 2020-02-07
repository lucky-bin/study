# MySQL 调优
## 开启慢查询
```sql
-- 查看是否开启慢查询
show variables like '%slow%';

-- 开启慢查询
set global slow_query_log = on;

-- 设置慢查询时间为0.3s
show variables like 'long_query_time';
set long_query_time = 0.3;

-- 开启 show profile
show variables 'profiling';
set profiling = true;

-- 获取慢查询
show profiles;
-- 根据 queryid 获取
show profile for query queryid;
-- 整理profile
SELECT state, SUM(duration) AS Total_R,
ROUND(100 * SUM(duration) / (SELECT SUM(duration) FROM information_schema.profiling WHERE query_id = 1), 2) AS Pct_R,
COUNT(*) as Calls, SUM(duration) /COUNT(*) AS "R/Call"
FROM information_schema.profiling
WHERE query_id = queryid GROUP BY state ORDER BY total_r DESC;
```

## 慢查询日志统计 pt-query-digest
```shell
wget percona.com/get/pt-query-digest

chmod u+x pt-query-digest

pt-query-digest mysql慢查询日志文件目录
```
