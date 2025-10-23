package builder;

import builder.world.OverlayBuilder;
import builder.world.SpawnerDetails;
import builder.world.PlayerDetails;
import builder.world.CabbageDetails;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Unit tests for OverlayBuilder class.
 */
public class OverlayBuilderTest {

    @Test
    public void testGetSectionValidSection() throws IOException {
        String content = ":testlabel:\nline1\nline2\nend;\n:anotherlabel:\nother\nend;";
        List<String> result = OverlayBuilder.getSection("testlabel", content);
        
        Assert.assertEquals("Should find 2 lines", 2, result.size());
        Assert.assertEquals("First line should match", "line1", result.get(0));
        Assert.assertEquals("Second line should match", "line2", result.get(1));
    }

    @Test
    public void testGetSectionCaseInsensitive() throws IOException {
        String content = ":TESTLABEL:\nline1\nEND;";
        List<String> result = OverlayBuilder.getSection("testlabel", content);
        
        Assert.assertEquals("Should find 1 line", 1, result.size());
        Assert.assertEquals("Line should be lowercase", "line1", result.get(0));
    }

    @Test(expected = IOException.class)
    public void testGetSectionNotFound() throws IOException {
        String content = ":differentlabel:\nline1\nend;";
        OverlayBuilder.getSection("testlabel", content);
    }

    @Test
    public void testGetSectionEmptySection() throws IOException {
        String content = ":testlabel:\nend;";
        List<String> result = OverlayBuilder.getSection("testlabel", content);
        
        Assert.assertEquals("Should find 0 lines", 0, result.size());
    }

    @Test
    public void testExtractSpawnDetailsFromLine() {
        String line = "x:100 y:200 duration:300";
        SpawnerDetails result = OverlayBuilder.extractSpawnDetailsFromLine(line);
        
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertEquals("X should be 100", 100, result.getX());
        Assert.assertEquals("Y should be 200", 200, result.getY());
        Assert.assertEquals("Duration should be 300", 300, result.getDuration());
        Assert.assertTrue("toString should contain values", 
                          result.toString().contains("100") && 
                          result.toString().contains("200") && 
                          result.toString().contains("300"));
    }

    @Test
    public void testExtractPlayerDetailsFromLine() {
        String line = "x:50 y:75 coins:100 food:25";
        PlayerDetails result = OverlayBuilder.extractPlayerDetailsFromLine(line);
        
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertEquals("X should be 50", 50, result.getX());
        Assert.assertEquals("Y should be 75", 75, result.getY());
        Assert.assertEquals("Coins should be 100", 100, result.getStartingCoins());
        Assert.assertEquals("Food should be 25", 25, result.getStartingFood());
        Assert.assertTrue("toString should contain values", 
                          result.toString().contains("50") && 
                          result.toString().contains("75") && 
                          result.toString().contains("100") && 
                          result.toString().contains("25"));
    }

    @Test
    public void testExtractCabbageDetailsFromLine() throws Exception {
        // Use reflection to access private method
        String line = "x:10 y:20";
        java.lang.reflect.Method method = OverlayBuilder.class.getDeclaredMethod("extractCabbageDetailsFromLine", String.class);
        method.setAccessible(true);
        CabbageDetails result = (CabbageDetails) method.invoke(null, line);
        
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertEquals("X should be 10", 10, result.getX());
        Assert.assertEquals("Y should be 20", 20, result.getY());
        Assert.assertTrue("toString should contain values", 
                          result.toString().contains("10") && 
                          result.toString().contains("20"));
    }

    @Test
    public void testGetEagleSpawnDetailsFromString() throws IOException {
        String content = ":eaglespawner:\nx:100 y:200 duration:300\nx:150 y:250 duration:400\nend;";
        List<SpawnerDetails> result = OverlayBuilder.getEagleSpawnDetailsFromString(content);
        
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertEquals("Should have 2 eagle spawners", 2, result.size());
        Assert.assertEquals("First eagle X", 100, result.get(0).getX());
        Assert.assertEquals("First eagle Y", 200, result.get(0).getY());
        Assert.assertEquals("First eagle duration", 300, result.get(0).getDuration());
    }

    @Test
    public void testGetPigeonSpawnDetailsFromString() throws IOException {
        String content = ":pigeonspawner:\nx:50 y:60 duration:120\nend;";
        List<SpawnerDetails> result = OverlayBuilder.getPigeonSpawnDetailsFromString(content);
        
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertEquals("Should have 1 pigeon spawner", 1, result.size());
        Assert.assertEquals("Pigeon X", 50, result.get(0).getX());
        Assert.assertEquals("Pigeon Y", 60, result.get(0).getY());
        Assert.assertEquals("Pigeon duration", 120, result.get(0).getDuration());
    }

    @Test
    public void testGetMagpieSpawnDetailsFromString() throws IOException {
        String content = ":magpiespawner:\nx:80 y:90 duration:180\nend;";
        List<SpawnerDetails> result = OverlayBuilder.getMagpieSpawnDetailsFromString(content);
        
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertEquals("Should have 1 magpie spawner", 1, result.size());
        Assert.assertEquals("Magpie X", 80, result.get(0).getX());
        Assert.assertEquals("Magpie Y", 90, result.get(0).getY());
        Assert.assertEquals("Magpie duration", 180, result.get(0).getDuration());
    }

    @Test
    public void testGetPlayerDetailsFromFile() throws IOException {
        String content = ":chickenfarmer:\nx:100 y:150 coins:200 food:50\nend;";
        PlayerDetails result = OverlayBuilder.getPlayerDetailsFromFile(content);
        
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertEquals("Player X", 100, result.getX());
        Assert.assertEquals("Player Y", 150, result.getY());
        Assert.assertEquals("Player coins", 200, result.getStartingCoins());
        Assert.assertEquals("Player food", 50, result.getStartingFood());
    }

    @Test
    public void testGetCabbageSpawnDetailsFromString() throws IOException {
        String content = ":cabbages:\nx:10 y:20\nx:30 y:40\nend;";
        List<CabbageDetails> result = OverlayBuilder.getCabbageSpawnDetailsFromString(content);
        
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertEquals("Should have 2 cabbages", 2, result.size());
        Assert.assertEquals("First cabbage X", 10, result.get(0).getX());
        Assert.assertEquals("First cabbage Y", 20, result.get(0).getY());
        Assert.assertEquals("Second cabbage X", 30, result.get(1).getX());
        Assert.assertEquals("Second cabbage Y", 40, result.get(1).getY());
    }

    @Test
    public void testGetSectionMultipleWordsInLabels() throws IOException {
        String content = ":test label:\ndata\nend;";
        List<String> result = OverlayBuilder.getSection("test label", content);
        
        Assert.assertEquals("Should find 1 line", 1, result.size());
        Assert.assertEquals("Should match data", "data", result.get(0));
    }

    @Test
    public void testExtractSpawnDetailsWithDifferentOrder() {
        // The method expects fixed order: x y duration
        String line = "x:300 y:400 duration:500";
        SpawnerDetails result = OverlayBuilder.extractSpawnDetailsFromLine(line);
        
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertEquals("X should be 300", 300, result.getX());
        Assert.assertEquals("Y should be 400", 400, result.getY());
        Assert.assertEquals("Duration should be 500", 500, result.getDuration());
    }

    @Test
    public void testExtractPlayerDetailsWithDifferentOrder() {
        // The method expects fixed order: x y coins food
        String line = "x:60 y:80 coins:150 food:30";
        PlayerDetails result = OverlayBuilder.extractPlayerDetailsFromLine(line);
        
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertEquals("X should be 60", 60, result.getX());
        Assert.assertEquals("Y should be 80", 80, result.getY());
        Assert.assertEquals("Coins should be 150", 150, result.getStartingCoins());
        Assert.assertEquals("Food should be 30", 30, result.getStartingFood());
    }

    @Test(expected = IOException.class)
    public void testGetEagleSpawnDetailsFromStringMissingSection() throws IOException {
        String content = ":differentspawner:\nx:100 y:200 duration:300\nend;";
        OverlayBuilder.getEagleSpawnDetailsFromString(content);
    }

    @Test(expected = IOException.class)
    public void testGetPigeonSpawnDetailsFromStringMissingSection() throws IOException {
        String content = ":differentspawner:\nx:100 y:200 duration:300\nend;";
        OverlayBuilder.getPigeonSpawnDetailsFromString(content);
    }

    @Test(expected = IOException.class)
    public void testGetMagpieSpawnDetailsFromStringMissingSection() throws IOException {
        String content = ":differentspawner:\nx:100 y:200 duration:300\nend;";
        OverlayBuilder.getMagpieSpawnDetailsFromString(content);
    }

    @Test(expected = IOException.class)
    public void testGetPlayerDetailsFromFileMissingSection() throws IOException {
        String content = ":differentfarmer:\nx:100 y:150 coins:200 food:50\nend;";
        OverlayBuilder.getPlayerDetailsFromFile(content);
    }

    @Test(expected = IOException.class)
    public void testGetCabbageSpawnDetailsFromStringMissingSection() throws IOException {
        String content = ":vegetables:\nx:10 y:20\nend;";
        OverlayBuilder.getCabbageSpawnDetailsFromString(content);
    }

    /**
     * Test the private load method with invalid file extension
     * Covers line 23 mutations (conditional checks)
     */
    @Test
    public void testLoadMethodWithInvalidExtension() throws Exception {
        Method loadMethod = OverlayBuilder.class.getDeclaredMethod("load", String.class);
        loadMethod.setAccessible(true);
        
        try {
            loadMethod.invoke(null, "test.txt");
            Assert.fail("Should throw IllegalArgumentException for non-.details file");
        } catch (java.lang.reflect.InvocationTargetException e) {
            Assert.assertTrue("Should throw IllegalArgumentException", 
                e.getCause() instanceof IllegalArgumentException);
            Assert.assertTrue("Message should mention .details", 
                e.getCause().getMessage().contains(".details"));
        }
    }

    /**
     * Test the private load method with valid .details file
     * Covers line 23 and line 26 mutations (conditional and return value)
     */
    @Test
    public void testLoadMethodWithValidFile() throws Exception {
        // Create a temporary .details file
        Path tempFile = Files.createTempFile("test", ".details");
        String testContent = "test content\nline 2";
        Files.writeString(tempFile, testContent);
        
        try {
            Method loadMethod = OverlayBuilder.class.getDeclaredMethod("load", String.class);
            loadMethod.setAccessible(true);
            
            String result = (String) loadMethod.invoke(null, tempFile.toString());
            
            Assert.assertNotNull("Result should not be null", result);
            Assert.assertFalse("Result should not be empty", result.isEmpty());
            Assert.assertEquals("Content should match", testContent, result);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    /**
     * Test the private load method conditional with .details extension
     * Covers line 23 mutation (replaced equality check with false/true)
     */
    @Test
    public void testLoadMethodConditionalWithDetailsExtension() throws Exception {
        Path tempFile = Files.createTempFile("valid", ".details");
        Files.writeString(tempFile, "content");
        
        try {
            Method loadMethod = OverlayBuilder.class.getDeclaredMethod("load", String.class);
            loadMethod.setAccessible(true);
            
            // Should NOT throw exception for valid .details file
            String result = (String) loadMethod.invoke(null, tempFile.toString());
            Assert.assertNotNull("Should successfully load .details file", result);
        } catch (java.lang.reflect.InvocationTargetException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                Assert.fail("Should not throw IllegalArgumentException for valid .details file");
            }
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
}