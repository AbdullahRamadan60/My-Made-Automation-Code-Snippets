
@Test
    public void rotateCanvasImage() {

        driver.navigate().to("https://tools.cornerstonejs.org/examples/tools/rotate.html");

        driver.navigate().refresh();  // Need to make refresh to show the canvas content (!!!)

        try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}


        WebElement canvas = driver.findElement(By.cssSelector("canvas[class=\"cornerstone-canvas\"]"));


        File canvasImageBeforeRotation = canvas.getScreenshotAs(OutputType.FILE);   //Screenshot for the canvas before rotation and save it
        try
        {
            FileUtils.copyFile(canvasImageBeforeRotation, new File("C:\\temp\\canvasBefore.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Actions actions = new Actions(driver);

        actions.moveToElement(canvas).perform();   // It moves to the center of the element
        actions.clickAndHold(canvas).perform();
        actions.moveByOffset(-20, -30).perform();  //Move a little to left & up ,while still clicking and holding, to rotate

        try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}

        File canvasImageAfterRotation = canvas.getScreenshotAs(OutputType.FILE);   //Screenshot for the canvas after rotation and save it
        try {
            FileUtils.copyFile(canvasImageAfterRotation, new File("C:\\temp\\canvasAfter.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Then, we can make the following method to check that the percentage of the change is > 0 .. (We can also check the screenshot images manually)
        double percentage =  Utility.getDifferencePercentage(canvasImageBeforeRotation, canvasImageAfterRotation);
        Assert.assertTrue(percentage > 0 , "There's no change to the canvas");

    }
    
    
                         ////********* THE UTILITY CLASS FOR THIS TEST ***************\\\\\\\
    
    public class Utility
{
    public static double getDifferencePercentage(File img1 , File img2)
    {
        // Initially assigning null
        BufferedImage imgA = null;
        BufferedImage imgB = null;

        // Try block to check for exception
        try {
            
            // Reading files
            imgA = ImageIO.read(img1);
            imgB = ImageIO.read(img2);
        }

        // Catch block to check for exceptions
        catch (IOException e) {
            // Display the exceptions on console
            System.out.println(e);
        }

        // Assigning dimensions to image
        int width1 = imgA.getWidth();
        int width2 = imgB.getWidth();
        int height1 = imgA.getHeight();
        int height2 = imgB.getHeight();

        // Checking whether the images are of same size or
        // not
        double percentage = 0;
        if ((width1 != width2) || (height1 != height2))

            // Display message straightaway
            System.out.println("Error: Images dimensions"
                    + " mismatch");
        else {

            // By now, images are of same size

            long difference = 0;

            // treating images likely 2D matrix

            // Outer loop for rows(height)
            for (int y = 0; y < height1; y++) {

                // Inner loop for columns(width)
                for (int x = 0; x < width1; x++) {

                    int rgbA = imgA.getRGB(x, y);
                    int rgbB = imgB.getRGB(x, y);
                    int redA = (rgbA >> 16) & 0xff;
                    int greenA = (rgbA >> 8) & 0xff;
                    int blueA = (rgbA) & 0xff;
                    int redB = (rgbB >> 16) & 0xff;
                    int greenB = (rgbB >> 8) & 0xff;
                    int blueB = (rgbB) & 0xff;

                    difference += Math.abs(redA - redB);
                    difference += Math.abs(greenA - greenB);
                    difference += Math.abs(blueA - blueB);
                }
            }

            // Total number of red pixels = width * height
            // Total number of blue pixels = width * height
            // Total number of green pixels = width * height
            // So total number of pixels = width * height *
            // 3
            double total_pixels = width1 * height1 * 3;

            // Normalizing the value of different pixels
            // for accuracy

            // Note: Average pixels per color component
            double avg_different_pixels
                    = difference / total_pixels;

            // There are 255 values of pixels in total
            percentage = (avg_different_pixels / 255) * 100;

            // Lastly print the difference percentage
            System.out.println("Difference Percentage-->"
                    + percentage);
        }

        return percentage;
    }
}
