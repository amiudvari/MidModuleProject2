DROP TABLE if exists task;
CREATE TABLE task (
  task_id serial,
  task_name varchar(80) NOT NULL,          -- Name of the task
  priority varchar(50) NOT NULL,     	   -- Priority of task
  due_date date NOT NULL,       		   -- Due date
  completed boolean default false,              -- Status of the task - complete or not 
  description varchar(500)  NULL	       -- Description of task
);



INSERT INTO task (task_name, priority, due_date, completed)
VALUES ('X-ray Validation', 'Medium', '2024-07-31', false);

INSERT INTO task (task_name, priority, due_date, completed)
VALUES ('Micro Validation',	'High', '2024-05-01', false);

