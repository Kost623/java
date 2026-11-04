import Class.BaseEntity;

/*Інший клас сутності: перевіряємо, що однаковий id у різних класів не робить об'єкти рівними.*/
class OtherTestEntity extends BaseEntity
{
    OtherTestEntity(long id, String name)
    {
        super(id, name);
    }
}
