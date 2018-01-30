package kstarostin.thumbtack.net.netexam;

import org.junit.Test;

import kstarostin.thumbtack.net.netexam.ui.entry.Validator;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ValidatorTest {

    private Validator validator;

    public ValidatorTest(){
        validator = new Validator();
    }

    @Test
    public void invalidLogin(){
        assertEquals((Integer)R.string.login_error, validator.getErrorLogin(""));
    }
    @Test
    public void validLogin(){
        assertEquals(null, validator.getErrorLogin("aA10бБ"));
    }

    @Test
    public void validAuthorizationPassword(){
        assertEquals(null, validator.getErrorPassword("вывDsdDЛАЛУ13;№;№!!%"));
    }
    @Test
    public void invalidAuthorizationPassword(){
        assertEquals((Integer)R.string.password_length_error,
                validator.getErrorPassword("вВdD1!"));
    }

    @Test
    public void validRegistrationPassword(){
        assertEquals(null, validator.getErrorPassword("аЙоУRto#@354","аЙоУRto#@354"));
    }
    @Test
    public void invalidRegistrationPassword(){
        assertEquals((Integer)R.string.password_match_error,
                validator.getErrorPassword("кКRRrTt1#456", "кКRRrTt1o456"));
    }

    @Test
    public void validFirstName(){
        assertEquals(null, validator.getErrorFirstName("Миру - мир"));
    }
    @Test
    public void invalidFirstName(){
        assertEquals((Integer)R.string.person_error, validator.getErrorFirstName("Миру - миQ"));
    }

    @Test
    public void validLastName(){
        assertEquals(null, validator.getErrorLastName("Алевтин - Богомольский"));
    }
    @Test
    public void invalidLastName(){
        assertEquals((Integer)R.string.person_error,
                validator.getErrorLastName("Не совсем_то"));
    }

    @Test
    public void validPatronymic(){
        assertEquals(null, validator.getErrorPatronymic("Станиславович -"));
    }
    @Test
    public void invalidPatronymic(){
        assertEquals((Integer)R.string.person_error,
                validator.getErrorPatronymic("Станисл1кович"));
    }

    @Test
    public void validSemester(){
        assertEquals(null, validator.getErrorSemester("12"));
    }
    @Test
    public void invalidSemesterUsingSpace(){
        assertEquals((Integer)R.string.semester_error, validator.getErrorSemester("1 "));
    }
    @Test
    public void invalidSemesterUsingSpecialCharacter(){
        assertEquals((Integer)R.string.semester_error, validator.getErrorSemester("3^"));
    }
    @Test
    public void invalidSemesterUsingRussianAlphabet(){
        assertEquals((Integer)R.string.semester_error, validator.getErrorSemester("2б"));
    }
    @Test
    public void invalidSemesterUsingEnglishAlphabet(){
        assertEquals((Integer)R.string.semester_error, validator.getErrorSemester("10Y"));
    }

    @Test
    public void validGroup(){
        assertEquals(null, validator.getErrorGroup("ПИН-161"));
    }
    @Test
    public void invalidGroup(){
        assertEquals((Integer)R.string.group_error, validator.getErrorGroup("ПИН 161"));
    }

    @Test
    public void validPosition(){
        assertEquals(null, validator.getErrorPosition("Главный-главный преподаватель"));
    }
    @Test
    public void invalidPosition(){
        assertEquals((Integer)R.string.person_error, validator.getErrorPosition("Преподаватель - 1"));
    }

    @Test
    public void validDepartment(){
        assertEquals(null,
                validator.getErrorDepartment("Автоматические системы - программирование"));
    }
    @Test
    public void invalidDepartment(){
        assertEquals((Integer)R.string.person_error, validator.getErrorDepartment("АСОИУ!"));
    }

    @Test
    public void everythingWasIncorrect(){
        Validator validator = new Validator();
        if (validator.getErrorLogin("%")==null){
            fail();
        }
        assertEquals(false, validator.wasEverythingCorrect());
    }

    @Test
    public void everythingWasCorrect(){
        Validator validator = new Validator();
        if (validator.getErrorLogin("a")!=null){
            fail();
        }
        assertTrue(validator.wasEverythingCorrect());
    }
}