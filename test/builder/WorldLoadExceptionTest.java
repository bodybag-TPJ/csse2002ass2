package builder;

import org.junit.Assert;
import org.junit.Test;
import builder.world.WorldLoadException;

public class WorldLoadExceptionTest {

    @Test
    public void testConstructorMessageOnly() {
        WorldLoadException exception = new WorldLoadException("Test error");
        Assert.assertEquals("Message should match", "Test error", exception.getMessage());
    }

    @Test
    public void testGetMessageNoRowNoCol() {
        WorldLoadException exception = new WorldLoadException("Basic error");
        String message = exception.getMessage();
        Assert.assertEquals("Should return base message", "Basic error", message);
        Assert.assertFalse("Should not contain 'line'", message.contains("line"));
        Assert.assertFalse("Should not contain 'character'", message.contains("character"));
    }

    @Test
    public void testConstructorWithRow() {
        WorldLoadException exception = new WorldLoadException("Row error", 5);
        String message = exception.getMessage();
        Assert.assertTrue("Should contain base message", message.contains("Row error"));
        Assert.assertTrue("Should contain line number", message.contains("line"));
    }

    @Test
    public void testGetMessageWithRow() {
        WorldLoadException exception = new WorldLoadException("Error at row", 0);
        String message = exception.getMessage();
        Assert.assertEquals("Should format with row+1", "Error at row on line 1", message);
    }

    @Test
    public void testGetMessageWithRowAddition() {
        WorldLoadException exception = new WorldLoadException("Test", 5);
        String message = exception.getMessage();
        Assert.assertTrue("Should contain '6' (5+1)", message.contains("6"));
        Assert.assertTrue("Should contain 'on line'", message.contains("on line"));
    }

    @Test
    public void testConstructorWithRowAndColumn() {
        WorldLoadException exception = new WorldLoadException("Full error", 3, 7);
        String message = exception.getMessage();
        Assert.assertTrue("Should contain base message", message.contains("Full error"));
        Assert.assertTrue("Should contain line", message.contains("line"));
        Assert.assertTrue("Should contain character", message.contains("character"));
    }

    @Test
    public void testGetMessageWithRowAndCol() {
        WorldLoadException exception = new WorldLoadException("Error at position", 2, 4);
        String message = exception.getMessage();
        Assert.assertEquals("Should format with row+1 and col+1", 
            "Error at position on line 3, character 5", message);
    }

    @Test
    public void testGetMessageWithRowAndColAddition() {
        WorldLoadException exception = new WorldLoadException("Test", 10, 20);
        String message = exception.getMessage();
        Assert.assertTrue("Should contain '11' (10+1)", message.contains("11"));
        Assert.assertTrue("Should contain '21' (20+1)", message.contains("21"));
        Assert.assertTrue("Should contain 'line'", message.contains("line"));
        Assert.assertTrue("Should contain 'character'", message.contains("character"));
    }

    @Test
    public void testRowNotEqualNegativeOne() {
        WorldLoadException exception1 = new WorldLoadException("Test", 0);
        String message1 = exception1.getMessage();
        Assert.assertTrue("Row 0 should trigger row != -1", message1.contains("line"));
        
        WorldLoadException exception2 = new WorldLoadException("Test", 5);
        String message2 = exception2.getMessage();
        Assert.assertTrue("Row 5 should trigger row != -1", message2.contains("line"));
    }

    @Test
    public void testColNotEqualNegativeOne() {
        WorldLoadException exception1 = new WorldLoadException("Test", 0, 0);
        String message1 = exception1.getMessage();
        Assert.assertTrue("Col 0 should trigger col != -1", message1.contains("character"));
        
        WorldLoadException exception2 = new WorldLoadException("Test", 5, 10);
        String message2 = exception2.getMessage();
        Assert.assertTrue("Col 10 should trigger col != -1", message2.contains("character"));
    }

    @Test
    public void testRowAndColBothNotNegativeOne() {
        WorldLoadException exception = new WorldLoadException("Test", 3, 7);
        String message = exception.getMessage();
        Assert.assertTrue("Should contain line when both set", message.contains("line"));
        Assert.assertTrue("Should contain character when both set", message.contains("character"));
        Assert.assertTrue("Should contain row+1", message.contains("4"));
        Assert.assertTrue("Should contain col+1", message.contains("8"));
    }

    @Test
    public void testMessageNotEmpty() {
        WorldLoadException exception1 = new WorldLoadException("Message1");
        Assert.assertFalse("Message should not be empty", exception1.getMessage().isEmpty());
        
        WorldLoadException exception2 = new WorldLoadException("Message2", 5);
        Assert.assertFalse("Message should not be empty", exception2.getMessage().isEmpty());
        
        WorldLoadException exception3 = new WorldLoadException("Message3", 5, 10);
        Assert.assertFalse("Message should not be empty", exception3.getMessage().isEmpty());
    }

    @Test
    public void testGetMessageReturnsCorrectString() {
        WorldLoadException exception1 = new WorldLoadException("Error1");
        String msg1 = exception1.getMessage();
        Assert.assertNotNull("Message should not be null", msg1);
        Assert.assertTrue("Should contain base message", msg1.contains("Error1"));
        
        WorldLoadException exception2 = new WorldLoadException("Error2", 2);
        String msg2 = exception2.getMessage();
        Assert.assertNotNull("Message should not be null", msg2);
        Assert.assertTrue("Should contain base message", msg2.contains("Error2"));
        Assert.assertTrue("Should contain 'on line 3'", msg2.contains("on line 3"));
        
        WorldLoadException exception3 = new WorldLoadException("Error3", 1, 2);
        String msg3 = exception3.getMessage();
        Assert.assertNotNull("Message should not be null", msg3);
        Assert.assertTrue("Should contain base message", msg3.contains("Error3"));
        Assert.assertTrue("Should contain 'on line 2'", msg3.contains("on line 2"));
        Assert.assertTrue("Should contain 'character 3'", msg3.contains("character 3"));
    }

    @Test
    public void testRowAdditionZero() {
        WorldLoadException exception = new WorldLoadException("Test", 0);
        String message = exception.getMessage();
        Assert.assertTrue("0 + 1 should be 1", message.contains("line 1"));
    }

    @Test
    public void testColAdditionZero() {
        WorldLoadException exception = new WorldLoadException("Test", 0, 0);
        String message = exception.getMessage();
        Assert.assertTrue("0 + 1 should be 1 for row", message.contains("line 1"));
        Assert.assertTrue("0 + 1 should be 1 for col", message.contains("character 1"));
    }

    @Test
    public void testRowAdditionLargeNumber() {
        WorldLoadException exception = new WorldLoadException("Test", 99);
        String message = exception.getMessage();
        Assert.assertTrue("99 + 1 should be 100", message.contains("100"));
    }

    @Test
    public void testColAdditionLargeNumber() {
        WorldLoadException exception = new WorldLoadException("Test", 50, 99);
        String message = exception.getMessage();
        Assert.assertTrue("50 + 1 should be 51", message.contains("51"));
        Assert.assertTrue("99 + 1 should be 100", message.contains("100"));
    }

    @Test
    public void testConditionalRowEqualsNegativeOne() {
        WorldLoadException exception = new WorldLoadException("Test message only");
        String message = exception.getMessage();
        Assert.assertEquals("Should return base message when row is -1", "Test message only", message);
    }

    @Test
    public void testConditionalRowNotEqualsNegativeOneColEqualsNegativeOne() {
        WorldLoadException exception = new WorldLoadException("Test", 5);
        String message = exception.getMessage();
        Assert.assertTrue("Should include row info", message.contains("on line"));
        Assert.assertFalse("Should not include col info", message.contains("character"));
    }

    @Test
    public void testConditionalBothNotEqualsNegativeOne() {
        WorldLoadException exception = new WorldLoadException("Test", 5, 10);
        String message = exception.getMessage();
        Assert.assertTrue("Should include row info", message.contains("on line"));
        Assert.assertTrue("Should include col info", message.contains("character"));
    }
}

