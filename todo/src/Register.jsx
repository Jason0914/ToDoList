// userService.js
const BASE_URL = 'http://localhost:8080/api/users';

export const registerUser = async(userData) => {
  const response = await fetch(`${BASE_URL}/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData),
  });
  const result = await response.json();
  if(result.status === 200){
    return result.data;
  }
  throw new Error(result.message);
};

export const loginUser = async(userData) => {
  const response = await fetch(`${BASE_URL}/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData),
  });
  const result = await response.json();
  if(result.status === 200){
    return result.data;
  }
  throw new Error(result.message);
};
