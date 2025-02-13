import { useState } from 'react';
import { loginUser } from '../services/userService';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const userData = await loginUser({ username, password });
      // 登入成功後可將使用者資料存到全域狀態或 localStorage
      console.log('登入成功:', userData);
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div>
      <h2>登入</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={handleLogin}>
        <div>
          <label>用戶名：</label>
          <input value={username} onChange={(e) => setUsername(e.target.value)} />
        </div>
        <div>
          <label>密碼：</label>
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        </div>
        <button type="submit">登入</button>
      </form>
    </div>
  );
}

export default Login;
