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

const BASE_URL = "http://localhost:8080/api/users";

// 統一的請求頭
const headers = {
  "Content-Type": "application/json",
  Accept: "application/json",
};

// 統一的錯誤處理
const handleResponse = async (response) => {
  if (!response.ok) {
    // 先嘗試解析錯誤訊息
    try {
      const errorData = await response.json();
      throw new Error(errorData.message || "操作失敗");
    } catch (e) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
  }
  const result = await response.json();
  if (result.status === 200) {
    return result.data;
  }
  throw new Error(result.message || "操作失敗");
};

// 用戶註冊
export const registerUser = async (userData) => {
  try {
    console.log("Sending registration request:", userData); // 調試用
    const response = await fetch(`${BASE_URL}/register`, {
      method: "POST",
      headers: headers,
      credentials: "include", // 如果需要 cookies
      body: JSON.stringify(userData),
    });
    return handleResponse(response);
  } catch (error) {
    console.error("Registration error:", error); // 調試用
    throw error;
  }
};

// 用戶登入
export const loginUser = async (userData) => {
  try {
    console.log("Sending login request:", userData); // 調試用
    const response = await fetch(`${BASE_URL}/login`, {
      method: "POST",
      headers: headers,
      credentials: "include", // 如果需要 cookies
      body: JSON.stringify(userData),
    });
    return handleResponse(response);
  } catch (error) {
    console.error("Login error:", error); // 調試用
    throw error;
  }
};

// 檢查用戶名是否存在
export const checkUsername = async (username) => {
  try {
    const response = await fetch(`${BASE_URL}/exists/username/${username}`);
    return handleResponse(response);
  } catch (error) {
    console.error("Username check error:", error);
    throw error;
  }
};

// 檢查郵箱是否存在
export const checkEmail = async (email) => {
  try {
    const response = await fetch(`${BASE_URL}/exists/email/${email}`);
    return handleResponse(response);
  } catch (error) {
    console.error("Email check error:", error);
    throw error;
  }
};
