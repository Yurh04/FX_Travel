// src/utils/globalStore.js
import { useUserStore } from '../store/user'

let _userStore = null

export const initGlobalStore = (app) => {
    // 已经初始化过则直接返回实例
    if (_userStore && !app) return { userStore: _userStore }

    if (app) {
        // 首次初始化
        _userStore = useUserStore()

        // 开发环境调试
        if (process.env.NODE_ENV === 'development') {
            window.__userStore = _userStore
            console.debug('Global store mounted to window.__userStore')
        }
    }

    if (!_userStore) throw new Error('Store 未初始化')

    return { userStore: _userStore }
}

export const getUserStore = () => {
    if (!_userStore) throw new Error('请先调用 initGlobalStore()')
    return _userStore
}