package com.example.clientservice.models;

import java.io.Serializable;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Client implements Serializable {
	private Integer id;
	private String surname;
	private String name;
	private String phone;
	private Date date;
}
