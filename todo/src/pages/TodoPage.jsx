import { useEffect, useState } from "react";
import TodoList from "../components/TodoList";
import TodoInput from "../components/TodoInput";
import "bootstrap/dist/css/bootstrap.min.css";
import {
  fetchTodos,
  addTodo,
  updateTodo,
  deleteTodo,
} from "../services/todoService";

function TodoPage() {
  const [todos, setTodos] = useState([]);
  const [todo, setTodo] = useState("");

  useEffect(() => {
    fetchTodos()
      .then(setTodos)
      .catch((error) => console.error("Error fetching todos:", error));
  }, []);

  const handleAdd = async () => {
    if (!todo) return;
    const newTodo = { text: todo, completed: false };

    try {
      const addedTodo = await addTodo(newTodo);
      setTodos([...todos, addedTodo]);
      setTodo("");
    } catch (error) {
      console.error("Error adding todo:", error);
    }
  };

  const handleChange = (e) => setTodo(e.target.value);

  const toggleCompletion = async (id) => {
    try {
      const updatedTodo = todos.find((todo) => todo.id === id);
      if (!updatedTodo) return;
      updatedTodo.completed = !updatedTodo.completed;
      await updateTodo(updatedTodo);
      setTodos([...todos]);
    } catch (error) {
      console.error("Error updating todo:", error);
    }
  };

  const handleDelete = (id) => {
    deleteTodo(id)
      .then(() => setTodos(todos.filter((todo) => todo.id !== id)))
      .catch((error) => console.error("Error deleting todo:", error));
  };

  return (
    <div className="container mt-5">
      <h1 className="text-center mb-4">我的待辦事項</h1>
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
