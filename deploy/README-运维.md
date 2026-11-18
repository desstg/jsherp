# jshERP 运维手册（群晖 NAS）

> 位置：`/volume1/docker/jsherp/README-运维.md`
> 最后更新：2026-11-18
> 适用：DSM 7.3.2 / Container Manager / x86_64（Intel Celeron J4125，4GB 内存）

---

## 一、当前部署概览

| 项目 | 值 |
|---|---|
| 访问地址 | **http://192.168.31.4:8080** |
| 默认租户 | `desstg`（密码 `123456`） |
| 系统自带租户 | `jsh`（带演示数据，密码 `123456`） |
| 超管账号 | `admin`（密码 `123456`，tenant_id=0） |
| 部署目录 | `/volume1/docker/jsherp` |
| 源码版本 | jishenghua/jshERP 官方仓库 `38ad91e`（2026-11-11） |
| 后端版本 | SpringBoot 2.0.0 + JDK 8，端口 9999 |
| 前端版本 | Vue 2.7 + Ant Design Vue 1.5.2，nginx 80 |

### 四个容器

| 容器名 | 镜像 | 作用 | 端口 |
|---|---|---|---|
| `jsherp-mysql` | `mysql:8.0` | 数据库 `jsh_erp`（32 张表） | 仅内网 3306 |
| `jsherp-redis` | `redis:6.2-alpine` | 会话、验证码、缓存 | 仅内网 6379 |
| `jsherp-backend` | `jsherp-backend:local`（327MB） | SpringBoot 后端 | 9999 |
| `jsherp-frontend` | `jsherp-frontend:local`（65.7MB） | nginx 托管前端 + 反代 API | **8080** |

全部由 **`docker-compose.local.yml`** 一个 compose 项目管理，项目名 `jsherp`。

---

## 二、目录结构

```
/volume1/docker/jsherp/
├── docker-compose.local.yml    ← 唯一的编排文件（改配置只碰这个）
├── .env                        ← 密码/端口配置（权限 600，别外传）
├── init/
│   └── 01-jsh_erp.sql          ← 32 张表 + 演示数据（仅首次启动时执行）
├── src/                        ← 官方源码（12MB）
│   ├── jshERP-boot/            ← 后端源码
│   ├── jshERP-web/             ← 前端源码
│   └── deploy/
│       ├── Dockerfile.backend
│       ├── Dockerfile.frontend
│       ├── maven-settings.xml   ← 阿里云 Maven 镜像
│       └── nginx.conf           ← 反代规则
├── src.tar.gz                  ← 源码包（备份用）
├── mysql-data/                 ← 【关键】所有业务数据（约 222MB）
├── redis-data/                 ← 会话数据（2.6MB）
├── upload/                     ← 上传的附件
├── export/                     ← 报表导出临时文件
├── tmp/                        ← tomcat 临时目录
├── nginx-logs/                 ← nginx 访问日志
├── build-backend.log           ← 后端构建日志
├── build-frontend.log          ← 前端构建日志
└── backup-*.sql                ← 历史备份
```

**备份只需要两个目录：`mysql-data/` 和 `upload/`。**

---

## 三、日常操作

### 3.1 查看状态

```bash
cd /volume1/docker/jsherp

# 容器状态
sudo docker compose -f docker-compose.local.yml ps

# 健康检查（三个 healthy 一个 starting 属正常）
sudo docker ps --filter name=jsherp --format "{{.Names}}\t{{.Status}}"

# 资源占用
sudo docker stats --no-stream --format "{{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}" $(sudo docker ps -q --filter name=jsherp)
```

### 3.2 启停

```bash
cd /volume1/docker/jsherp

# 全部启动
sudo docker compose -f docker-compose.local.yml up -d

# 全部停止（数据不丢）
sudo docker compose -f docker-compose.local.yml stop

# 全部重启
sudo docker compose -f docker-compose.local.yml restart

# 只重启后端（改配置后常用）
sudo docker compose -f docker-compose.local.yml restart backend

# 停止并删除容器（数据卷保留）
sudo docker compose -f docker-compose.local.yml down
```

### 3.3 看日志

```bash
cd /volume1/docker/jsherp

# 实时跟随后端日志（排错主力）
sudo docker compose -f docker-compose.local.yml logs -f --tail=100 backend

# 只看错误
sudo docker logs jsherp-backend 2>&1 | grep -iE "error|exception" | tail -30

# MySQL 日志
sudo docker logs jsherp-mysql --tail=50

# nginx 访问日志
sudo tail -f nginx-logs/access.log
```

### 3.4 进数据库

```bash
# 交互式
sudo docker exec -it jsherp-mysql mysql -uroot -pJshErp2026Synology jsh_erp

# 单条查询（注意加 --default-character-set 否则中文乱码）
sudo docker exec jsherp-mysql mysql --default-character-set=utf8mb4 \
  -uroot -pJshErp2026Synology -e "select id,login_name,tenant_id from jsh_erp.jsh_user;"

# 看某租户的商品数
sudo docker exec jsherp-mysql mysql --default-character-set=utf8mb4 \
  -uroot -pJshErp2026Synology -e "select count(*) from jsh_erp.jsh_material where tenant_id=150;"
```

---

## 四、重新部署

### 场景 A：只改了配置（.env 或 compose）

```bash
cd /volume1/docker/jsherp
sudo vi .env                                  # 改密码/端口
sudo docker compose -f docker-compose.local.yml up -d   # 自动重建受影响容器
```

### 场景 B：源码更新了，要重新构建

```bash
cd /volume1/docker/jsherp

# 1. 备份数据库（重要！）
sudo docker exec jsherp-mysql mysqldump -uroot -pJshErp2026Synology \
  --single-transaction --routines --triggers jsh_erp \
  > backup-$(date +%Y%m%d-%H%M%S).sql

# 2. 上传新源码包（本地 scp 上来），然后解压覆盖
sudo tar -xzf src.tar.gz -C src --overwrite

# 3. 只重建改动的镜像（先后端，再前端，别并行）
sudo docker compose -f docker-compose.local.yml build backend
sudo docker compose -f docker-compose.local.yml build frontend

# 4. 滚动更新
sudo docker compose -f docker-compose.local.yml up -d

# 5. 验证
curl -s -o /dev/null -w "%{http_code}\n" http://127.0.0.1:8080/
```

### 场景 C：彻底重装（清空所有业务数据）

```bash
cd /volume1/docker/jsherp
sudo docker compose -f docker-compose.local.yml down
sudo rm -rf mysql-data redis-data upload export tmp
sudo docker compose -f docker-compose.local.yml up -d
# MySQL 首次启动会自动执行 init/01-jsh_erp.sql
```

### 场景 D：只重建前端（后端没动）

```bash
cd /volume1/docker/jsherp
sudo docker compose -f docker-compose.local.yml build frontend
sudo docker compose -f docker-compose.local.yml up -d frontend
```

---

## 五、从本地电脑推文件到群晖

群晖 **禁用了 SFTP**，`scp` 必须加 `-O`（回退到传统 SCP 协议）：

```powershell
# 在 Windows PowerShell 里（需要装了 Git 的 OpenSSH）
& 'C:\Program Files\Git\usr\bin\scp.exe' -O `
   'E:\本地\文件.tar.gz' `
   'pindy@192.168.31.4:/volume1/docker/jsherp/'
```

如果不加 `-O` 会报 `subsystem request failed on channel 0`。

---

## 六、已知问题与对策

### 6.1 Docker pull 卡死（拉不动镜像）

**症状**：`docker pull` 一直显示 `Waiting`，永远不完成。

**原因**：这台 NAS 的 DNS 把 `registry-1.docker.io` 污染成了 Facebook 的 IP（`2a03:2880:...`），加上 daemon 代理配置缺认证，导致 pull 挂死。

**对策一：用国内镜像源前缀**
```bash
sudo docker pull docker.m.daocloud.io/library/mysql:8.0
sudo docker tag docker.m.daocloud.io/library/mysql:8.0 mysql:8.0
```

**对策二：用手工拉取脚本（终极方案）**

目录里的 `fetch2.sh` 可以绕过 daemon 直接调 registry API：

```bash
cd /volume1/docker/jsherp
sudo sh fetch2.sh docker.m.daocloud.io library/mysql 8.0 /volume1/docker/jsherp/w-mysql
sudo docker load -i /volume1/docker/jsherp/w-mysql.tar
```

原理：curl 拿 token → 取 manifest → 走 307 重定向下 blob → 组装成 `docker load` 格式。

### 6.2 后端构建 OOM（exit 137）

**症状**：`docker build` 报 `returned a non-zero code: 137`。

**原因**：J4125 只有 4GB 内存，Maven/JVM 默认堆按物理内存 1/4 取约 980MB，编译时被内核杀掉。

**对策**：Dockerfile 里已经设了限制，别改掉：
```dockerfile
# 后端
RUN MAVEN_OPTS="-Xmx512m -XX:MaxMetaspaceSize=256m" mvn ...

# 前端
ENV NODE_OPTIONS="--max_old_space_size=1024"
```
如果还是 OOM，先停掉其它占内存的容器再构建。

### 6.3 Maven 下载卡在失效仓库

**症状**：构建日志里反复出现 `Downloading from spring-snapshot:` / `rabbit-milestone:`，每个依赖先失败三次才回落，慢几个数量级。

**原因**：SpringBoot 2.0 的 parent pom 继承了 `repo.spring.io/snapshot`、`dl.bintray.com` 等已失效仓库。

**对策**：`deploy/maven-settings.xml` 里必须用 `mirrorOf>*<`（不是 `central`）：
```xml
<mirror>
  <id>aliyun-all</id>
  <url>https://maven.aliyun.com/repository/public</url>
  <mirrorOf>*</mirrorOf>   <!-- 关键：* 而不是 central -->
</mirror>
```

### 6.4 前端构建 EEXIST 失败

**症状**：`npm ERR! EEXIST: file already exists /usr/local/bin/yarn`

**原因**：`node:16-bullseye` 镜像自带 yarn 1.22.19，不要再 `npm i -g yarn`。

### 6.5 新注册租户登录后只有"首页"

**症状**：注册成功、能登录，但菜单空白只有一个"首页"。

**原因**：jshERP 的 bug。注册只分配内置角色 `roleId=10`，而该角色的 `RoleFunctions` 权限只有首页；且 `FunctionService.getCurrentTenantFunIdList()` 用 `list.get(0)` 只取第一条，永远取到残缺记录。

**对策**：已修复（`jsh_user_business` 表 id=32 那条记录的 value 已补全为完整菜单列表）。**此修复对所有新建租户永久生效。**

如果以后又被覆盖，重新执行：
```bash
sudo docker exec jsherp-mysql mysql -uroot -pJshErp2026Synology -e "
update jsh_erp.jsh_user_business set value='[210][225][211][241][32][33][199][242][38][41][200][201][239][202][40][232][233][197][44][203][204][205][206][212][1][13][14][15][16][234][236][243][260][21][22][23][220][247][24][25][217][218][26][194][195][31][198][59][207][208][209][259][226][227][228][229][235][237][244][246][248]' where id=32;"
```

### 6.6 注册提示"登录名在本系统已存在"

**原因**：`jsh_user.login_name` 全局唯一。之前用过的名字（哪怕账号已失效）不能复用。

**对策**：换个登录名，或先删除旧用户：
```bash
# 查
sudo docker exec jsherp-mysql mysql -uroot -pJshErp2026Synology \
  -e "select id,login_name,tenant_id from jsh_erp.jsh_user where login_name='xxx';"
# 删（含角色关联）
sudo docker exec jsherp-mysql mysql -uroot -pJshErp2026Synology -e "
delete from jsh_erp.jsh_user_business where key_id=<用户id> and type='UserRole';
delete from jsh_erp.jsh_user where id=<用户id>;"
```

### 6.7 验证码看不清 / 注册登录失败

验证码是 **4 位字母数字**（如 `loks`、`nmj7`），大小写不敏感但容易输错。

排查是否验证码问题：
```bash
# 拿 uuid
RESP=$(curl -s http://127.0.0.1:8080/jshERP-boot/user/randomImage)
UUID=$(echo "$RESP" | python3 -c "import sys,json;print(json.load(sys.stdin)['data']['uuid'])")

# 从 Redis 读真实验证码
sudo docker exec jsherp-redis redis-cli -a JshRedis2026Synology \
  --no-auth-warning get "captcha_codes:$UUID"
```

### 6.8 前端页面白屏 / 报错

先强刷浏览器（`Ctrl+F5`），因为构建后 js 文件名 hash 会变。

还不行就看后端日志有没有 500：
```bash
sudo docker logs jsherp-backend --tail=100 2>&1 | grep -iE "error|exception"
```

---

## 七、备份与恢复

### 7.1 数据库备份（推荐每天）

```bash
cd /volume1/docker/jsherp
sudo docker exec jsherp-mysql mysqldump -uroot -pJshErp2026Synology \
  --single-transaction --routines --triggers jsh_erp \
  > backup-$(date +%Y%m%d-%H%M%S).sql
```

保留最近 7 天：
```bash
ls -t backup-*.sql | tail -n +8 | xargs -r rm --
```

### 7.2 恢复数据库

```bash
cd /volume1/docker/jsherp
sudo docker exec -i jsherp-mysql mysql -uroot -pJshErp2026Synology jsh_erp \
  < backup-20261118-120000.sql
```

### 7.3 整机备份（推荐用 Hyper Backup）

备份 `/volume1/docker/jsherp/mysql-data` 和 `/volume1/docker/jsherp/upload` 两个目录即可。

---

## 八、安全提醒

1. **改掉默认密码**：`desstg`/`jsh`/`admin` 都是 `123456`，登录后立刻改
2. **`.env` 权限**：`chmod 600 .env`
3. **不要暴露 9999**：后端端口映射在 compose 里是 `"${BACKEND_PORT:-9999}:9999"`，如果不需要排障，把 `.env` 里的 `BACKEND_PORT` 改成不映射
4. **公网访问走 HTTPS**：用群晖反向代理 + 证书，不要直接暴露 8080
5. **定期备份** `mysql-data/` 和 `upload/`

---

## 九、常用 SQL 速查

```sql
-- 所有租户
select tenant_id,login_name,user_num_limit,expire_time from jsh_tenant;

-- 所有用户
select id,login_name,tenant_id,isystem,ismanager,status from jsh_user order by tenant_id;

-- 某租户的商品数 / 客户数 / 单据数
select
  (select count(*) from jsh_material where tenant_id=150) 商品,
  (select count(*) from jsh_supplier where tenant_id=150) 往来单位,
  (select count(*) from jsh_depot_head where tenant_id=150) 单据;

-- 某租户所有单据（看类型分布）
select type,sub_type,count(*) from jsh_depot_head where tenant_id=150 group by type,sub_type;

-- 商品价格（四套价）
select m.name, e.bar_code, e.commodity_unit,
       e.purchase_decimal 采购价, e.commodity_decimal 零售价,
       e.wholesale_decimal 批发价, e.low_decimal 最低售价
from jsh_material m join jsh_material_extend e on e.material_id=m.id
where m.tenant_id=150;

-- 库存
select m.name, s.current_number 当前库存, s.tenant_id
from jsh_material_current_stock s join jsh_material m on m.id=s.material_id
where s.tenant_id=150;
```

---

## 十、紧急处置

**系统打不开**
```bash
cd /volume1/docker/jsherp
sudo docker compose -f docker-compose.local.yml ps     # 看谁挂了
sudo docker compose -f docker-compose.local.yml up -d   # 全部拉起
sudo docker compose -f docker-compose.local.yml logs --tail=50 backend
```

**数据库起不来**
```bash
sudo docker logs jsherp-mysql --tail=50
# 常见：mysql-data 权限问题
sudo chmod -R 777 mysql-data
```

**磁盘满了**
```bash
df -h /volume1
sudo docker system df           # 看镜像占多少
sudo docker image prune -a      # 清理无用镜像（会删掉 !!! 慎用，会删掉未使用的镜像）
ls -t backup-*.sql | tail -n +8 | xargs -r rm --   # 清理旧备份
```

**整台 NAS 重启后容器没起来**
```bash
cd /volume1/docker/jsherp
sudo docker compose -f docker-compose.local.yml up -d
# 容器都有 restart: unless-stopped，正常会自动起来
```
