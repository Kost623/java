package Service;

import Class.Reader;

import java.util.List;

/*Інтерфейс сутності Reader: друге поле сортування (рік народження) є лише в читача.*/
public interface ReaderService extends CRUDService<Reader>
{
    // Спочатку за іменем, а при збігу імен за роком народження (за зростанням)
    List<Reader> findAllSortedByNameAndByYearOfBirth();
}