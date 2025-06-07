# Spring Boot ChatGPT 集成项目

## 功能特性
- OpenAI API 密钥配置
- RESTful 对话接口
- 上下文管理（待实现）

## 接口文档
```
POST /api/chat
Content-Type: application/json

请求体：
{
  "prompt": "你好"
}

响应：
"你好！有什么可以帮助您的吗？"
```

## 运行要求
1. 设置环境变量：
```
export OPENAI_API_KEY=your-api-key
```
2. 启动应用：
```
mvn spring-boot:run
```