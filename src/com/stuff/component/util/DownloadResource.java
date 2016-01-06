package com.stuff.component.util;

import java.io.InputStream;

import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;

public class DownloadResource implements ConnectorResource
{

	private String fileName;
	private String mimeType;
	private DownloadStream stream;

	
	public DownloadResource(InputStream in, String filename, String mimeType)
	{
		this.fileName = filename;
		this.mimeType = mimeType;
		stream = new DownloadStream(in, mimeType, filename);
	}
	
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setStream(DownloadStream stream) {
		this.stream = stream;
	}

	@Override
	public String getMIMEType() {
		return mimeType;
	}

	@Override
	public DownloadStream getStream() {
		return stream;
	}

	@Override
	public String getFilename() {
		return fileName;
	}
	
}
