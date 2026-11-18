# jshERP 进销存（管伊佳ERP）· Docker 构建与群晖部署

基于 [jishenghua/jshERP](https://github.com/jishenghua/jshERP) 官方源码（Apache-2.0，可商用），
补齐官方缺失的 **Dockerfile、compose 编排、GitHub Actions 自动构建**，
用于在群晖 NAS 或任意 Docker 主机上一键部署。

镜像由 GitHub Actions 云端构建，**产物发布到 GHCR**：

```
ghcr.io/desstg/jsherp-backend:latest     # SpringBoot 2.0 + JDK8
ghcr.io/desstg/jsherp-frontend:latest    # Vue 2.7 + nginx
```

> 支持零售（会员/收款/找零/零售价）+ 批发（客户/批发价/含税/账期欠款/折扣/订金），
> 14 类单据，多仓库调拨盘点，组装拆卸，应收应付往来账。

---

## 一、最省事：直接用现成镜像部署

镜像已构建好，不需要本地编译。

### 1.1 群晖上准备目录

```bash
ssh 用户名@群晖IP
sudo mkdir -p /volume1/docker/jsherp/init
cd /volume1/docker/jsherp
```

### 1.2 上传三个文件

用 File Station 或 `scp -O`（群晖禁用了 SFTP，`-O` 不能省）：

```powershell
& 'C:\Program Files\Git\usr\bin\scp.exe' -O docker-compose.yml      用户名@群晖IP:/volume1/docker/jsherp/
& 'C:\Program Files\Git\usr\bin\scp.exe' -O .env.example            用户名@群晖IP:/volume1/docker/jsherp/
& 'C:\Program Files\Git\usr\bin\scp.exe' -O init/01-jsh_erp.sql     用户名@群晖IP:/volume1/docker/jsherp/init/
```

### 1.3 配置密码

```bash
cd /volume1/docker/jsherp
cp .env.example .env
vi .env          # 改 MYSQL_ROOT_PASSWORD、REDIS_PASSWORD
chmod 600 .env
```

### 1.4 建数据目录并启动

```bash
cd /volume1/docker/jsherp
mkdir -p mysql-data redis-data upload export tmp nginx-logs
sudo chmod -R 777 mysql-data redis-data upload export tmp nginx-logs

sudo docker compose pull
sudo docker compose up -d
sudo docker compose logs -f backend      # 看到 Started ErpApplication 即可
```

### 1.5 访问

```
http://群晖IP:8080
租户：jsh
用户：admin
密码：123456
```

登录后立刻改密码。

---

## 二、GHCR 包要设为公开（否则拉不动）

GitHub → 你的头像 → **Your packages** → 分别点 `jsherp-backend`、`jsherp-frontend`
→ 右下 **Package settings** → **Danger Zone** → **Change visibility** → **Public**

两个包都要改。

不想公开就在群晖登录 GHCR（Container Manager → 注册表 → 新增 `ghcr.io`，
密码用带 `read:packages` 的 Personal Access Token）。

---

## 三、代码改了就自动构建

改完源码直接推：

```bash
git add -A
git commit -m "fix: xxx"
git push
```

Actions 自动触发：仓库 → **Actions** → `build-ghcr`，约 **8-12 分钟**。
成功后群晖上更新：

```bash
cd /volume1/docker/jsherp
sudo docker compose pull
sudo docker compose up -d
```

数据卷 `mysql-data`、`upload` 不受影响。

**手动重跑**：Actions → build-ghcr → Run workflow。
ARM 机型（DS220j 等）在 `platforms` 填 `linux/arm64`。

---

## 四、不想用云端镜像？本地构建

`deploy/docker-compose.local.yml` 带 `build:` 段，可在本机构建：

```bash
cd deploy
sudo docker compose -f docker-compose.local.yml build backend
sudo docker compose -f docker-compose.local.yml build frontend
sudo docker compose -f docker-compose.local.yml up -d
```

**注意三点**（都已在 Dockerfile 里处理，改动前先看）：

| 项目 | 约束 | 原因 |
|---|---|---|
| 内存 | `MAVEN_OPTS=-Xmx512m`、`NODE_OPTIONS=--max_old_space_size=1024` | 4GB NAS 上不加会被 OOM kill（exit 137） |
| Maven 源 | `mirrorOf>*<` 而非 `central` | SpringBoot 2.0 继承了已失效的 `repo.spring.io/snapshot`、`dl.bintray.com`，写 `central` 会每个依赖失败三次才回落 |
| Node 版本 | 必须 `node:16` | Vue CLI 3 + webpack 4，Node 17+ 报 `ERR_OSSL_EVP_UNSUPPORTED` |

先后端后前端，**不要并行**。

---

## 五、目录说明

```
.
├── docker-compose.yml          # 【群晖用这个】拉 GHCR 镜像
├── .env.example                # 配置模板
├── init/01-jsh_erp.sql         # 32 张表 + 演示数据（首次启动执行）
├── .github/workflows/
│   └── build-ghcr.yml          # Actions：推 main 自动构建并发布 GHCR
├── deploy/                     # 官方仓库没有这些，是本项目新增
│   ├── Dockerfile.backend      # maven:3.8.6-openjdk-8 → eclipse-temurin:8-jre
│   ├── Dockerfile.frontend     # node:16-bullseye → nginx:1.27-alpine
│   ├── maven-settings.xml      # 阿里云 Maven 镜像
│   ├── nginx.conf              # 反代 /jshERP-boot → backend:9999
│   ├── docker-compose.local.yml# 本地构建版编排
│   ├── docker-compose.yml      # GHCR 版编排（同根目录那份）
│   ├── .env.example
│   └── init/01-jsh_erp.sql
├── jshERP-boot/                # 官方后端源码（未修改）
└── jshERP-web/                 # 官方前端源码（未修改）
```

**源码保持原样，一行未改。** 所有部署相关文件都在 `deploy/` 和 `.github/`。

---

## 六、技术栈

| 组件 | 版本 |
|---|---|
| SpringBoot | 2.0.0.RELEASE |
| JDK | 8 |
| MyBatis-Plus | 3.0.7.1 |
| MySQL | 8.0 |
| Redis | 6.2 |
| Vue | 2.7.16 |
| Ant Design Vue | 1.5.2 |
| 开源协议 | Apache-2.0（商用无约束） |

---

## 七、常见问题

| 现象 | 原因 | 处理 |
|---|---|---|
| `Bind mount failed: 'mysql-data' does not exist` | 数据目录没建 | `mkdir -p mysql-data ...` |
| `pull access denied / 401` | GHCR 包还是私有 | 见第二节设为 Public |
| `exec format error` | 镜像架构不符 | Run workflow 时 platforms 改成群晖的架构 |
| `subsystem request failed on channel 0` | 群晖禁用 SFTP | `scp -O` |
| 构建 exit 137 | 内存不够 | 确认 Dockerfile 里的内存限制还在；停掉其它容器 |
| 注册后只有「首页」菜单 | jshERP 已知 bug | 见 `deploy/README-运维.md` 第 6.5 节 |
| 登录提示「验证码不能为空」 | 未带验证码 | 验证码是 4 位字母数字，注意大小写 |

更详细的排错、备份、SQL 速查见 [`deploy/README-运维.md`](deploy/README-运维.md)。

---

## 八、上游更新怎么同步

```bash
git remote add upstream https://github.com/jishenghua/jshERP.git   # 仅首次
git fetch upstream
git merge upstream/master
git push        # 自动触发 Actions 重新构建
```

冲突时保留 `deploy/`、`.github/`、`docker-compose.yml` 即可。

---

## 许可

- 本项目新增的 Dockerfile / compose / workflow：MIT
- jshERP 源码：Apache-2.0，版权归原作者 [jishenghua](https://github.com/jishenghua) 所有
