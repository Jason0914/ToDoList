/**
 * WEB API User Rest CRUD
 * ------------------------------------------------------------
 * POST   "http://localhost:8080/api/users/register"                     註冊用戶
 * POST   "http://localhost:8080/api/users/login"                        用戶登入
 * GET    "http://localhost:8080/api/users/{username}"                   根據用戶名查詢用戶資料
 * GET    "http://localhost:8080/api/users/exists/username/{username}"   檢查用戶名是否已存在
 * GET    "http://localhost:8080/api/users/exists/email/{email}"         檢查信箱是否已存在
 * ------------------------------------------------------------
 */

const BASE_URL = 'http://localhost:8080/api/users';

// 用戶註冊
export const registerUser = async (userData) => {
  const response = await fetch(`${BASE_URL}/register`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(userData),
  });
  const result = await response.json();
  if(result.status === 200){
    return result.data;
  }
  throw new Error(result.message);
};

// 用戶登入
export const loginUser = async (userData) => {
  const response = await fetch(`${BASE_URL}/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(userData),
  });
  const result = await response.json();
  if(result.status === 200){
    return result.data;
  }
  throw new Error(result.message);
};
