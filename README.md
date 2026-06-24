# War Thunder Vehicle Management Backend

战争雷霆车辆管理系统后端服务，为 [warthunder-vue](https://github.com/kurt-akimichidenisov/warthunder-vue) 提供API支持。
![项目演示视频](2026-06-24%2018-15-48.mp4)

## 技术栈

- **框架**: Spring Boot 3.2.0
- **数据库**: MySQL 8.0+
- **ORM**: Spring Data JPA
- **安全**: Spring Security + JWT
- **API文档**: SpringDoc OpenAPI (Swagger UI)
- **构建工具**: Maven

## 项目结构

```
src/main/java/com/warthunder/vehicle/
├── controller/          # REST API控制器
│   ├── AuthController       # 用户认证（登录/注册）
│   ├── CrewController       # 乘员管理
│   ├── DashboardController  # 仪表盘统计
│   ├── PlayerStatsController # 玩家统计
│   ├── PurchaseController   # 购买管理
│   └── VehicleController    # 车辆管理
├── service/             # 业务逻辑层
│   └── impl/              # 业务实现类
├── repository/          # 数据访问层（JPA）
├── entity/              # 数据库实体
├── dto/                 # 数据传输对象
├── config/              # 配置类
│   ├── CorsConfig          # 跨域配置
│   ├── SecurityConfig      # 安全配置
│   └── SwaggerConfig       # Swagger配置
├── util/                # 工具类
│   ├── JwtUtil             # JWT工具
│   ├── JwtAuthenticationFilter # JWT过滤器
│   └── UserContext         # 用户上下文
├── exception/           # 异常处理
│   └── GlobalExceptionHandler # 全局异常处理
├── common/              # 通用响应
│   ├── Result              # 统一响应封装
│   └── PageResult          # 分页响应封装
└── WarThunderVehicleManagementApplication.java  # 启动类
```

## 功能模块

| 模块 | 功能 |
|------|------|
| **用户认证** | 登录、注册、用户信息管理 |
| **车辆管理** | 车辆查询、详情、统计 |
| **乘员管理** | 乘员创建、分配、详情 |
| **购买系统** | 车辆购买、余额管理 |
| **玩家统计** | 等级、战绩、排行 |
| **仪表盘** | 数据概览统计 |

## 快速开始

### 环境要求

- Java 17+
- MySQL 8.0+
- Maven 3.8+

### 数据库配置

创建数据库并修改 `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/warthunderdb?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 运行项目

```bash
# 开发模式运行
mvn spring-boot:run

# 或打包后运行
mvn clean package
java -jar target/vehicle-management-0.0.1-SNAPSHOT.jar
```

### 访问地址

- **API地址**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI文档**: http://localhost:8080/v3/api-docs

## API接口

### 认证接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/auth/register` | POST | 用户注册 |
| `/api/auth/login` | POST | 用户登录 |

### 车辆接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/vehicles` | GET | 分页查询车辆列表 |
| `/api/vehicles/{id}` | GET | 查询车辆详情 |

### 乘员接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/crews` | GET | 查询当前用户乘员列表 |
| `/api/crews` | POST | 创建乘员 |
| `/api/crews/{id}` | GET | 查询乘员详情 |

### 购买接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/purchases/vehicle` | POST | 购买车辆 |
| `/api/purchases/balance` | POST | 充值余额 |

### 统计接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/stats/player` | GET | 获取玩家统计 |
| `/api/stats/rank` | GET | 获取玩家排行 |

### 仪表盘接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/dashboard` | GET | 获取仪表盘数据 |

## 前端项目

本项目为以下前端项目提供后端支持：

- **前端仓库**: [warthunder-vue](https://github.com/kurt-akimichidenisov/warthunder-vue)

## 许可证

MIT License
