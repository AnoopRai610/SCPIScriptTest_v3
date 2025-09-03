package com.sap.cpi.test.vm;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sap.cpi.test.util.Parser;
import com.sap.cpi.test.util.RegisterService;
import com.sap.it.api.mapping.ValueMappingApi;

class ValueMappingApiLoader  implements Parser {
	
	@Override
	public void loadFile(String path) throws Exception {
		RegisterService.register(ValueMappingApi.class.getName(), new ValueMappingApiHandler());
		File file = new File(path);
		List<File> zipFiles = new ArrayList<File>();

		if (file.isDirectory())
			zipFiles = Arrays.asList(file.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return (pathname.getName().toLowerCase().endsWith(".zip"));
				}
			}));
		else
			zipFiles.add(file);

		zipFiles.forEach(F -> {
			Document doc = null;
			ZipFile zip = null;
			try {
				zip = new ZipFile(F);
				Enumeration<? extends ZipEntry> entries = zip.entries();
				InputStream stream = null;
				while (entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();
					if (entry.getName().equals("value_mapping.xml"))
						stream = zip.getInputStream(entry);
				}
				doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
			} catch (Exception e) {
				throw new RuntimeException("Unable to load VM from " + F.getName() + e.getMessage());
			}

			NodeList nl = doc.getElementsByTagName("group");

			for (int i = 0; i < nl.getLength(); i++) {
				if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
					List<Entry> entries = new ArrayList<>();
					
					Element eElement = (Element) nl.item(i);
					NodeList nlEntity = eElement.getElementsByTagName("entry");
					entries.add(createEntry((Element) nlEntity.item(0)));
					entries.add(createEntry((Element) nlEntity.item(1)));
					
					Group group = new Group(eElement.getAttribute("id"),entries);
					ValueMappingApiImpl.addGroup(group);
				}
			}
			
			if(zip!=null) {
				try {
					zip.close();
				} catch (IOException e) {
					throw new RuntimeException("Unable to close " + F.getName() + e.getMessage());
				}
			}				
		});
	}

	private static Entry createEntry(Element entryEl) {
		Entry entry = new Entry(entryEl.getElementsByTagName("agency").item(0).getTextContent().trim(),
				entryEl.getElementsByTagName("schema").item(0).getTextContent().trim(),
				entryEl.getElementsByTagName("value").item(0).getTextContent().trim());
		
		if(entryEl.hasAttribute("isDefault") && entryEl.getAttribute("isDefault").equals("true")) {
			entry.setDefault(true);
		}
		
		return entry;
	}

}
