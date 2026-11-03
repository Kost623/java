package test;

import Class.BaseEntity;

/*Мінімальна сутність для тестів сервісу: id задається явно.*/
class TestEntity extends BaseEntity
{
    TestEntity(long id, String name)
    {
        super(id, name);
    }
}