import React from "react";
import { Link } from "react-router-dom";

function Home() {
  return (
    <div className="text-center mt-5">
      <h1>歡迎使用 Todo App</h1>
      <p>請先登入或註冊以繼續使用</p>
      <div className="mt-4">
        <Link to="/login">
          <button className="btn btn-primary m-2">登入</button>
        </Link>
        <Link to="/register">
          <button className="btn btn-secondary m-2">註冊</button>
        </Link>
      </div>
    </div>
  );
}

export default Home;
