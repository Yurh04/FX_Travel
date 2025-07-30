# FX_Travel

FX_Travel 项目由两个部分组成：

- **Spring Boot 后端**：位于 `src/main/java/org/fxtravel/fxspringboot`，提供 REST 接口和业务逻辑;
- **Vue 前端**：位于 `frontend`目录，基于 Vite 构建和运行。

## 后端目录结构

```
src/main/java/org/fxtravel/fxspringboot
├── controller   # 控制器实现 API 接口
├── service      # 业务层，inter 接口，impl 实现
├── mapper       # MyBatis Plus 数据层
├── pojo         # DTO 和实体
├── event        # 事件系统
├── utils        # 工具类
└── common       # 公共枚举等
```

## 前端启动

在 `frontend` 目录下：

```bash
npm run dev   # 启动 Vite 开发服务
```

构建生成生产文件：

```bash
npm run build
```

## 构建与运行

### 后端

使用 Maven 构建：

```bash
mvn clean package
```

构建完成后可通过 jar 文件运行：

```bash
java -jar target/FXSpringboot-0.0.1-SNAPSHOT.jar
```

或者直接使用

```bash
mvn spring-boot:run
```

### 前端

使用 Vite 开发方式即上文的 `npm run dev`，构建后的文件在 `frontend/dist`。

## 部署指引

1. 将后端构建成 jar 安装到服务器或部署平台;
2. 前端通过 `npm run build` 获得的 `dist` 目录可以传到静态服务器，或与后端一起打包部署;
3. 配置 `application.yml`，确保数据库、邮箱等信息正确。

