
const TOKEN_KEY = 'auth-token';

/**
 * 获取 token
 */
export function getToken(): string | null {
  return localStorage.getItem(TOKEN_KEY);
}

/**
 * 设置 token
 * @param token - 登录返回的 token
 */
export function setToken(token: string): void {
  localStorage.setItem(TOKEN_KEY, token);
}

/**
 * 删除 token（例如登出时）
 */
export function removeToken(): void {
  localStorage.removeItem(TOKEN_KEY);
}
