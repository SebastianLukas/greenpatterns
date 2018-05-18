package pr.toolkit.dtw;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WordCutter {

	/**
	 * 
	 * @param locationPath path to dir which contains all the svg's
	 * @param imagePath path to dir which contains the given jpg's
	 * @param outputPath path to dir where the cut out words should be stored
	 */
	public void cutOutWords(String locationPath, String imagePath, String outputPath) {
		try {
			File dir = new File(locationPath);
			File[] listOfFiles = dir.listFiles();

			for (File f : listOfFiles) {
				if (f.isFile()) {
					File inputFile = f;
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(inputFile);
					doc.getDocumentElement().normalize();
					NodeList nList = doc.getElementsByTagName("path");

					for (int i = 0; i < nList.getLength(); i++) {
						Node nNode = nList.item(i);
						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) nNode;
							BufferedImage source = ImageIO.read(new File(
									imagePath + f.getName().substring(0, 3)
											+ ".jpg"));

							GeneralPath clip = new GeneralPath();

							String[] boundingPoints = eElement
									.getAttribute("d").split(" ");
							for (int j = 0; j < boundingPoints.length; j++) {
								// moveto
								if (boundingPoints[j].equals("M")) {
									clip.moveTo(
											Double.parseDouble(boundingPoints[j + 1]),
											Double.parseDouble(boundingPoints[j + 2]));
									j += 2;
								}
								// lineto
								else if (boundingPoints[j].equals("L")) {
									clip.lineTo(
											Double.parseDouble(boundingPoints[j + 1]),
											Double.parseDouble(boundingPoints[j + 2]));
									j += 2;
								}
								// closepath
								else if (boundingPoints[j].equals("Z")) {
									clip.closePath();
								} else {
									System.out
											.println("Error unknown path element.");
								}

							}
							Rectangle2D bounds = clip.getBounds2D();
							BufferedImage img = new BufferedImage(
									(int) Math.ceil(bounds.getWidth()),
									(int) Math.ceil(bounds.getHeight()),
									BufferedImage.TYPE_INT_ARGB);
							Graphics2D g2d = img.createGraphics();
							clip.transform(AffineTransform
									.getTranslateInstance(bounds.getMinX()
											* -1.0, bounds.getMinY() * -1.0));
							g2d.setClip(clip);
							g2d.translate(bounds.getMinX() * -1.0,
									bounds.getMinY() * -1.0);
							g2d.drawImage(source, 0, 0, null);
							g2d.dispose();

							ImageIO.write(img, "png", new File(outputPath
									+ eElement.getAttribute("id") + ".jpg"));

						}
					}
				} else if (f.isDirectory()) {
					System.out.println("Directory " + f.getName());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
