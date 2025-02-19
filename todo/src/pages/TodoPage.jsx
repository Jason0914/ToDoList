import { useEffect, useState } from "react";
import TodoList from "../components/TodoList";
import TodoInput from "../components/TodoInput";
import {
  fetchTodos,
  addTodo,
  updateTodo,
  deleteTodo,
} from "../services/todoService";

function TodoPage() {
  const [todos, setTodos] = useState([]);
  const [todo, setTodo] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    fetchTodos()
      .then(setTodos)
      .catch((error) => {
        console.error("Error fetching todos:", error);
        setError("加載待辦事項時出錯");
      });
  }, []);

  const handleAdd = async () => {
    if (!todo.trim()) return; // 防止空白輸入

    try {
      const newTodo = {
        text: todo,
        completed: false,
      };

      const addedTodo = await addTodo(newTodo);
      setTodos([...todos, addedTodo]);
      setTodo("");
      setError("");
    } catch (error) {
      console.error("Error adding todo:", error);
      setError("新增待辦事項時出錯");
    }
  };

  const handleChange = (e) => setTodo(e.target.value);

  const toggleCompletion = async (id) => {
    try {
      const todoToUpdate = todos.find((todo) => todo.id === id);
      if (!todoToUpdate) return;

      const updatedTodo = {
        ...todoToUpdate,
        completed: !todoToUpdate.completed,
      };

      await updateTodo(updatedTodo);
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

  const handleDelete = async (id) => {
    try {
      await deleteTodo(id);
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
      {error && (
        <div className="alert alert-danger" role="alert">
          {error}
        </div>
      )}
      <TodoInput todo={todo} onChange={handleChange} onAdd={handleAdd} />
      <TodoList
        todos={todos}
        onToggleCompletion={toggleCompletion}
        onDelete={handleDelete}
      />
    </div>
  );
}

export default TodoPage;
