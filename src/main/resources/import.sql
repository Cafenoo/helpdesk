insert into user (email, first_name, last_name, password, role)
values ('user1_mogilev@yopmail.com', 'Empl 1', 'Empl Sur 1',
        '$2a$04$8PqRT.IZrdgUkAgdJD/wvOj6N1DqmoFWzhs7zIymXEyxTBR1VNANK', 'EMPLOYEE');
insert into user (email, first_name, last_name, password, role)
values ('user2_mogilev@yopmail.com', 'Empl 2', 'Empl Sur 2',
        '$2a$04$8PqRT.IZrdgUkAgdJD/wvOj6N1DqmoFWzhs7zIymXEyxTBR1VNANK', 'EMPLOYEE');
insert into user (email, first_name, last_name, password, role)
values ('manager1_mogilev@yopmail.com', 'Man 1', 'Man Sur 1',
        '$2a$04$8PqRT.IZrdgUkAgdJD/wvOj6N1DqmoFWzhs7zIymXEyxTBR1VNANK', 'MANAGER');
insert into user (email, first_name, last_name, password, role)
values ('manager2_mogilev@yopmail.com', 'Man 2', 'Man Sur 2',
        '$2a$04$8PqRT.IZrdgUkAgdJD/wvOj6N1DqmoFWzhs7zIymXEyxTBR1VNANK', 'MANAGER');
insert into user (email, first_name, last_name, password, role)
values ('engineer1_mogilev@yopmail.com', 'Eng 1', 'Eng Sur 1',
        '$2a$04$8PqRT.IZrdgUkAgdJD/wvOj6N1DqmoFWzhs7zIymXEyxTBR1VNANK', 'ENGINEER');
insert into user (email, first_name, last_name, password, role)
values ('engineer2_mogilev@yopmail.com', 'Eng 2', 'Eng Sur 2',
        '$2a$04$8PqRT.IZrdgUkAgdJD/wvOj6N1DqmoFWzhs7zIymXEyxTBR1VNANK', 'ENGINEER');

insert into category (name)
values ('Application & Services');
insert into category (name)
values ('Benefits & Paper Work');
insert into category (name)
values ('Hardware & Software');
insert into category (name)
values ('People Management');
insert into category (name)
values ('Security & Access');
insert into category (name)
values ('Workplaces & Facilities');

insert into ticket (created_on, desired_resolution_date, name, description, assignee_id, owner_id,
                    state, category_id, urgency, approver_id)
values ('2021-12-01', '2021-12-10', 'Ticket 1', 'Desc 1', 1, 4, 'NEW', 2, 'LOW', 6);
insert into ticket (created_on, desired_resolution_date, name, description, assignee_id, owner_id,
                    state, category_id, urgency, approver_id)
values ('2021-08-02', '2021-09-10', 'Ticket 2', 'Desc 2', 5, 3, 'DONE', 1, 'AVERAGE', 1);
insert into ticket (created_on, desired_resolution_date, name, description, assignee_id, owner_id,
                    state, category_id, urgency, approver_id)
values ('2021-10-15', '2021-11-05', 'Ticket 3', 'Desc 3', 2, 1, 'DRAFT', 1, 'HIGH', 4);
insert into ticket (created_on, desired_resolution_date, name, description, assignee_id, owner_id,
                    state, category_id, urgency, approver_id)
values ('2021-09-06', '2021-09-29', 'Ticket 4', 'Desc 4', 2, 1, 'NEW', 1, 'CRITICAL', 4);
insert into ticket (created_on, desired_resolution_date, name, description, assignee_id, owner_id,
                    state, category_id, urgency, approver_id)
values ('2021-09-06', '2021-10-25', 'Ticket 5', 'Desc 5', 2, 1, 'IN_PROGRESS', 1, 'CRITICAL', 4);
insert into ticket (created_on, desired_resolution_date, name, description, assignee_id, owner_id,
                    state, category_id, urgency, approver_id)
values ('2021-09-06', '2021-10-25', 'Ticket 6', 'Desc 6', 2, 1, 'DECLINED', 1, 'CRITICAL', 4);
