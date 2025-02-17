package com.example.todolist.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //自動生成getter/setter方法，toString()，equals、hashCode方法
@AllArgsConstructor //自動生成包含所有參數的建構子
@NoArgsConstructor //自動生成無參數的建構子
@Entity //標記這個類別是一個實體類，會對應到資料庫的表格
//@Table(name="todo")//預設名稱，指定資料表名稱
public class Todo {
	
	@Id//標記這個欄位是主鍵
	@GeneratedValue(strategy = GenerationType.IDENTITY)//由資料庫來決定id生成策略，主鍵生成策略
	private Long id;
	
	private String text;
	
	private Boolean completed;
	
	//添加與user的關聯
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id",nullable = false)
	private User user;
}
