import { useEffect, useState } from "react";
import TodoList from "../components/TodoList";
import TodoInput from "../components/TodoInput";
import {
  fetchTodos,
  addTodo,
  updateTodo,
  deleteTodo,
} from "../services/todoService";

/**
 * 待辦事項頁面組件
 *
 * 管理待辦事項的顯示、創建、更新和刪除功能。
 * 作為待辦事項管理的中央控制器，協調各個子組件和服務調用。
 *
 * @returns {JSX.Element} 待辦事項頁面
 */
function TodoPage() {
  /**
   * 待辦事項列表狀態
   */
  const [todos, setTodos] = useState([]);

  /**
   * 新待辦事項輸入狀態
   */
  const [todo, setTodo] = useState("");

  /**
   * 錯誤信息狀態
   */
  const [error, setError] = useState("");

  /**
   * 頁面載入時獲取待辦事項
   */
  useEffect(() => {
    // 調用 API 獲取所有待辦事項
    fetchTodos()
      .then(setTodos) // 成功時更新待辦事項列表
      .catch((error) => {
        console.error("Error fetching todos:", error);
        setError("加載待辦事項時出錯");
      });
  }, []); // 空依賴數組表示僅在組件掛載時執行一次

  /**
   * 處理添加待辦事項
   * 創建新的待辦事項並更新列表
   */
  const handleAdd = async () => {
    if (!todo.trim()) return; // 防止空白輸入

    try {
      // 構建新待辦事項數據
      const newTodo = {
        text: todo,
        completed: false,
      };

      // 調用 API 添加待辦事項
      const addedTodo = await addTodo(newTodo);

      // 更新本地待辦事項列表
      setTodos([...todos, addedTodo]);

      // 清空輸入框和錯誤信息
      setTodo("");
      setError("");
    } catch (error) {
      console.error("Error adding todo:", error);
      setError("新增待辦事項時出錯");
    }
  };

  /**
   * 處理輸入框變化
   *
   * @param {Object} e - 事件對象
   */
  const handleChange = (e) => setTodo(e.target.value);

  /**
   * 切換待辦事項完成狀態
   *
   * @param {number} id - 待辦事項 ID
   */
  const toggleCompletion = async (id) => {
    try {
      // 查找要更新的待辦事項
      const todoToUpdate = todos.find((todo) => todo.id === id);
      if (!todoToUpdate) return;

      // 構建更新數據，切換完成狀態
      const updatedTodo = {
        ...todoToUpdate,
        completed: !todoToUpdate.completed,
      };

      // 調用 API 更新待辦事項
      await updateTodo(updatedTodo);

      // 更新本地待辦事項列表
      setTodos(
        todos.map((todo) =>
          todo.id === id ? { ...todo, completed: !todo.completed } : todo
        )
      );
      setError("");
    } catch (error) {
      console.error("Error updating todo:", error);
      setError("更新待辦事項時出錯");
    }
  };

  /**
   * 刪除待辦事項
   *
   * @param {number} id - 待辦事項 ID
   */
  const handleDelete = async (id) => {
    try {
      // 調用 API 刪除待辦事項
      await deleteTodo(id);

      // 更新本地待辦事項列表，移除已刪除項
      setTodos(todos.filter((todo) => todo.id !== id));
      setError("");
    } catch (error) {
      console.error("Error deleting todo:", error);
      setError("刪除待辦事項時出錯");
    }
  };

  return (
    <div className="container mt-5">
      <h1 className="text-center mb-4">我的待辦事項</h1>

      {/* 錯誤信息顯示 */}
      {error && (
        <div className="alert alert-danger" role="alert">
          {error}
        </div>
      )}

      {/* 待辦事項輸入組件 */}
      <TodoInput todo={todo} onChange={handleChange} onAdd={handleAdd} />

      {/* 待辦事項列表組件 */}
      <TodoList
        todos={todos}
        onToggleCompletion={toggleCompletion}
        onDelete={handleDelete}
      />
    </div>
  );
}

export default TodoPage;
