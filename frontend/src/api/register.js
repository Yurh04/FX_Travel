// src/api/register.js

import axios from 'axios'

// ———————— 创建 axios 实例（带 Session 支持） ————————
const request = axios.create({
    baseURL: '/api',
    timeout: 5000,
    withCredentials: true // 必须开启，以支持 HttpSession 登录态
})

// -------------------- 🔐 AuthController 接口 --------------------

/**
 * 1. 注册账号
 * POST /api/auth/register
 *
 * @param {Object} data
 *   {
 *     email: string,
 *     password: string,
 *     username?: string,    // 如果后端需要更多字段，可以在这里补充
 *     // … 其它注册时需要的字段
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   // ❌ 错误示例：多套一层大括号
 *   // register({ data: { email: 'a@b.com', password: '123456' } })
 *
 *   // ✅ 正确示例：直接把对象当作第一个参数
 *   register({
 *     email: 'a@b.com',
 *     password: '123456',
 *     username: '张三'
 *   })
 */
export const register = (data) => {
    return request.post('/auth/register', data)
}

/**
 * 2. 验证邮箱验证码
 * POST /api/auth/verify
 *
 * @param {Object} data
 *   {
 *     email: string,
 *     code: string
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   // ❌ 多套一层大括号会导致后端收到 { data: { … } }
 *   // verifyEmail({ data: { email: 'a@b.com', code: '1234' } })
 *
 *   // ✅ 直接传扁平对象
 *   verifyEmail({
 *     email: 'a@b.com',
 *     code: '1234'
 *   })
 */
export const verifyEmail = (data) => {
    return request.post('/auth/verify', data)
}

/**
 * 3. 登录
 * POST /api/auth/login
 *
 * @param {Object} data
 *   {
 *     email: string,
 *     password: string
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   // ❌ 错误：login({ data: { email: 'a@b.com', password: 'xxx' } })
 *
 *   // ✅ 正确
 *   login({
 *     email: 'a@b.com',
 *     password: '123456'
 *   })
 */
export const login = (data) => {
    return request.post('/auth/login', data)
}

/**
 * 4. 登出
 * POST /api/auth/logout
 *
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   logout()
 */
export const logout = () => {
    return request.post('/auth/logout')
}

/**
 * 5. 忘记密码（发送邮箱重置链接或验证码）
 * POST /api/auth/forgotPassword
 *
 * @param {string} email
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   // ❌ 错误示例：forgotPassword({ data: 'a@b.com' })
 *
 *   // ✅ 正确示例：因为接口指定了 { email } 结构，所以直接传一个对象即可
 *   forgotPassword('a@b.com')
 *
 *   // 底层实际发出的请求体为：{ email: 'a@b.com' }
 */
export const forgotPassword = (email) => {
    return request.post('/auth/forgotPassword', { email })
}

/**
 * 6. 通过邮箱验证码重置密码
 * POST /api/auth/resetPassword/ByVerificationCode
 *
 * @param {Object} data
 *   {
 *     email: string,
 *     code: string,
 *     password: string   // 新密码
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   // ❌ 错误示例：resetByVerificationCode({ data: { … } })
 *
 *   // ✅ 正确示例
 *   resetByVerificationCode({
 *     email: 'a@b.com',
 *     code: '1234',
 *     password: 'newPassword'
 *   })
 */
export const resetByVerificationCode = (data) => {
    return request.post('/auth/resetPassword/ByVerificationCode', data)
}

/**
 * 7. 通过旧密码重置密码
 * POST /api/auth/resetPassword/ByOldPassword
 *
 * @param {Object} data
 *   {
 *     email: string,
 *     oldPassword: string,
 *     newPassword: string
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   // ❌ 错误示例：resetByOldPassword({ data: { … } })
 *
 *   // ✅ 正确示例
 *   resetByOldPassword({
 *     email: 'a@b.com',
 *     oldPassword: 'old123',
 *     newPassword: 'new456'
 *   })
 */
export const resetByOldPassword = (data) => {
    return request.post('/auth/resetPassword/ByOldPassword', data)
}


// -------------------- 👤 UserController 接口 --------------------

/**
 * 1. 获取当前用户信息
 * GET /api/user/userdata
 *
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   getCurrentUser()
 *     .then(res => console.log(res.data))
 *     .catch(err => console.error(err))
 */
export const getCurrentUser = () => {
    return request.get('/user/userdata')
}

/**
 * 2. 更新当前用户信息
 * PUT /api/user/userdata/update
 *
 * @param {Object} data
 *   {
 *     username?: string,
 *     gender?: 'MALE' | 'FEMALE' | 'OTHER'
 *     // … 后端允许的字段，可以按需传
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   // ❌ 错误示例：updateUser({ data: { username: '张三', gender: 'MALE' } })
 *
 *   // ✅ 正确示例
 *   updateUser({
 *     username: '张三',
 *     gender: 'FEMALE'
 *   })
 */
export const updateUser = (data) => {
    return request.put('/user/userdata/update', data)
}

/**
 * 3. 授予管理员权限（仅管理员可用）
 * PUT /api/user/grant
 *
 * @param {string|number} userId
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   // ❌ 错误示例：grantAdminRole({ data: 123 })
 *
 *   // ✅ 正确示例：把 userId 包在一个简单对象里
 *   grantAdminRole(123)
 *
 *   // 底层发出的请求体：{ userId: 123 }
 */
export const grantAdminRole = (userId) => {
    return request.put('/user/grant', { userId })
}


// ———————— 导出所有方法 ————————
export default {
    register,
    verifyEmail,
    login,
    logout,
    forgotPassword,
    resetByVerificationCode,
    resetByOldPassword,
    getCurrentUser,
    updateUser,
    grantAdminRole
}
