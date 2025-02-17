package com.example.todolist.service;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.todolist.exception.TodoNotFoundException;
import com.example.todolist.model.dto.TodoDTO;
import com.example.todolist.model.entity.Todo;
import com.example.todolist.model.entity.User;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.repository.UserRepository;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<TodoDTO> getAllTodosByUserId(Long userId) {
        List<Todo> todos = todoRepository.findByUserId(userId);
        return todos.stream()
                   .map(todo -> modelMapper.map(todo, TodoDTO.class))
                   .collect(Collectors.toList());
    }

    @Override
    public TodoDTO createTodo(TodoDTO todoDTO, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用戶不存在"));
            
        Todo todo = modelMapper.map(todoDTO, Todo.class);
        todo.setUser(user);
        Todo savedTodo = todoRepository.save(todo);
        return modelMapper.map(savedTodo, TodoDTO.class);
    }

    @Override
    public TodoDTO updateTodo(TodoDTO todoDTO, Long userId) throws TodoNotFoundException {
        Todo todo = todoRepository.findById(todoDTO.getId())
            .orElseThrow(() -> new TodoNotFoundException("查無資料"));
            
        if (!todo.getUser().getId().equals(userId)) {
            throw new TodoNotFoundException("無權限修改此待辦事項");
        }
        
        modelMapper.map(todoDTO, todo);
        Todo updatedTodo = todoRepository.save(todo);
        return modelMapper.map(updatedTodo, TodoDTO.class);
    }

    @Override
    public void deleteTodo(Long id, Long userId) throws TodoNotFoundException {
        Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new TodoNotFoundException("查無資料"));
            
        if (!todo.getUser().getId().equals(userId)) {
            throw new TodoNotFoundException("無權限刪除此待辦事項");
        }
        
        todoRepository.deleteById(id);
    }
}
