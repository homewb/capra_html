package viewComponent;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.xml.stream.XMLStreamException;

import routePlannerModel.RouteModel;
import routePlannerModel.RouteModelImp;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserFunction;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import dataModel.CapraPathNotFoundException;
import dataModel.LatLngException;

public class Main {

	private JFrame frame;
	
	private Browser browser = new Browser();
    private BrowserView browserView = new BrowserView(browser);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RouteModel model = new RouteModelImp();
					Main window = new Main(model);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main(RouteModel model) {
		initialize();
		initializeBrowser(model);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(50, 50, 1200, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().add(browserView);
		browser.loadURL("file:///" + System.getProperty("user.dir") + "/index.html");
	}
	
	private void initializeBrowser(final RouteModel model) {
		// ======== Function registration starts ==============
		browser.registerFunction("getTrainStation", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				String inputText_Origin = args[0].getString();
				String name = model.getTrainStationName(inputText_Origin);
				
				return JSValue.create(name);
			}
		});
		
		browser.registerFunction("run", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				
				try {
					model.calcPath();
					
					return JSValue.create("true");
				} catch (FileNotFoundException | XMLStreamException
						| LatLngException | CapraPathNotFoundException ex) {
					
					return JSValue.create(ex.getMessage());
				}
			}
		});
		
		browser.registerFunction("getStartLat", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				double stratLat = model.getStartLat();

				return JSValue.create(stratLat);
			}
		});

		browser.registerFunction("getStartLng", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				double startLng = model.getStartLng();

				return JSValue.create(startLng);
			}
		});

		browser.registerFunction("getEndLat", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				double endLat = model.getEndLat();

				return JSValue.create(endLat);
			}
		});

		browser.registerFunction("getEndLng", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				double endLng = model.getEndLng();

				return JSValue.create(endLng);
			}
		});

		browser.registerFunction("getFirstLat", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				Double methodValue = new Double(args[0].getNumber());
				int method = methodValue.intValue();

				double lat = model.getFirstPathLat(method);

				return JSValue.create(lat);
			}
		});

		browser.registerFunction("getFirstLng", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				Double methodValue = new Double(args[0].getNumber());
				int method = methodValue.intValue();

				double lng = model.getFirstPathLng(method);

				return JSValue.create(lng);
			}
		});

		browser.registerFunction("getLat", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				Double value = new Double(args[0].getNumber());
				int index = value.intValue();

				Double methodValue = new Double(args[1].getNumber());
				int method = methodValue.intValue();

				double lat = model.getPathLat(index, method);

				return JSValue.create(lat);
			}
		});

		browser.registerFunction("getLng", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				Double value = new Double(args[0].getNumber());
				int index = value.intValue();

				Double methodValue = new Double(args[1].getNumber());
				int method = methodValue.intValue();

				double lng = model.getPathLng(index, method);

				return JSValue.create(lng);
			}
		});

		browser.registerFunction("getPathSize", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				Double methodValue = new Double(args[0].getNumber());
				int method = methodValue.intValue();

				int size = model.getPathSize(method);

				return JSValue.create(size);
			}
		});

		browser.registerFunction("getMoaFirstLat", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				Double pathIndexValue = new Double(args[0].getNumber());
				int pathIndex = pathIndexValue.intValue();

				double lat = model.getMoaFirstPathLat(pathIndex);

				return JSValue.create(lat);
			}
		});

		browser.registerFunction("getMoaFirstLng", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				Double pathIndexValue = new Double(args[0].getNumber());
				int pathIndex = pathIndexValue.intValue();

				double lng = model.getMoaFirstPathLng(pathIndex);

				return JSValue.create(lng);
			}
		});

		browser.registerFunction("getMoaLat", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				Double stepIndexValue = new Double(args[0].getNumber());
				int stepIndex = stepIndexValue.intValue();

				Double pathIndexValue = new Double(args[1].getNumber());
				int pathIndex = pathIndexValue.intValue();

				double lat = model.getMoaPathLat(stepIndex, pathIndex);

				return JSValue.create(lat);
			}
		});

		browser.registerFunction("getMoaLng", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				Double stepIndexValue = new Double(args[0].getNumber());
				int stepIndex = stepIndexValue.intValue();

				Double pathIndexValue = new Double(args[1].getNumber());
				int pathIndex = pathIndexValue.intValue();

				double lng = model.getMoaPathLng(stepIndex, pathIndex);

				return JSValue.create(lng);
			}
		});

		browser.registerFunction("getMoaPathSize", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				Double pathIndexValue = new Double(args[0].getNumber());
				int pathIndex = pathIndexValue.intValue();

				int size = model.getMoaPathSize(pathIndex);

				return JSValue.create(size);
			}
		});

		browser.registerFunction("getMoaSize", new BrowserFunction() {
			public JSValue invoke(JSValue... args) {
				int size = model.getMoaSize();

				return JSValue.create(size);
			}
		});
		
		browser.registerFunction("getStartLat", new BrowserFunction() {
            public JSValue invoke(JSValue... args) {
                double stratLat = model.getStartLat();
            	
                return JSValue.create(stratLat);
            }
        });
        
        browser.registerFunction("getStartLng", new BrowserFunction() {
            public JSValue invoke(JSValue... args) {
            	double startLng = model.getStartLng();
                
                return JSValue.create(startLng);
            }
        });
        
        browser.registerFunction("getEndLat", new BrowserFunction() {
            public JSValue invoke(JSValue... args) {
            	double endLat = model.getEndLat();
                
                return JSValue.create(endLat);
            }
        });
        
        browser.registerFunction("getEndLng", new BrowserFunction() {
            public JSValue invoke(JSValue... args) {
            	double endLng = model.getEndLng();
                
                return JSValue.create(endLng);
            }
        });
        
        browser.registerFunction("getEdgeStartLat", new BrowserFunction() {
            public JSValue invoke(JSValue... args) {
            	Double value = new Double(args[0].getNumber());
            	int index = value.intValue();
                
            	double startLat = model.getEdgeStartLat(index);
            	
                return JSValue.create(startLat);
            }
        });
        
        browser.registerFunction("getEdgeStartLng", new BrowserFunction() {
            public JSValue invoke(JSValue... args) {
            	Double value = new Double(args[0].getNumber());
            	int index = value.intValue();
                
            	double startLng = model.getEdgeStartLng(index);
            	
                return JSValue.create(startLng);
            }
        });
        
        browser.registerFunction("getEdgeEndLat", new BrowserFunction() {
            public JSValue invoke(JSValue... args) {
            	Double value = new Double(args[0].getNumber());
            	int index = value.intValue();
                
            	double endLat = model.getEdgeEndLat(index);
            	
                return JSValue.create(endLat);
            }
        });
        
        browser.registerFunction("getEdgeEndLng", new BrowserFunction() {
            public JSValue invoke(JSValue... args) {
            	Double value = new Double(args[0].getNumber());
            	int index = value.intValue();
                
            	double endLng = model.getEdgeEndLng(index);
            	
                return JSValue.create(endLng);
            }
        });
        
        browser.registerFunction("getEdgeWeight", new BrowserFunction() {
            public JSValue invoke(JSValue... args) {
            	Double value = new Double(args[0].getNumber());
            	int index = value.intValue();
            	
            	int weight = model.getEdgeWeight(index);
                
                return JSValue.create(weight);
            }
        });
        
        browser.registerFunction("getEdgeSize", new BrowserFunction() {
            public JSValue invoke(JSValue... args) {
            	int size = model.getEdgeSize();
                
                return JSValue.create(size);
            }
        });
		// ======== Function registration ends ==============
	}

}
