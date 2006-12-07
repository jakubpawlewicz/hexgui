//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.htp;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.IOException;

//----------------------------------------------------------------------------

/** Sends HTP commands and parses the response. */
public class HtpController
{ 
    public HtpController(InputStream in, OutputStream out)
    {
	m_in = new BufferedReader(new InputStreamReader(in));
	m_out = new PrintStream(out);
	m_connected = true;
    }
    
    public void sendCommand(String cmd) throws HtpError
    {
	sendCommand(cmd, null);
    }

    public void sendCommand(String cmd, Runnable callback) throws HtpError
    {
	if (!m_connected) 
	    throw new HtpError("Hex Program Disconnected.");

	System.out.println("controller: sending '" + cmd.trim() + "'");
	m_out.print(cmd);
        handleResponse(callback);

	if (callback != null) {
	    System.out.println("controller: running callback!");
	    callback.run();
	}
    }

    public String getResponse()
    {
	return m_response;
    }

    private void handleResponse(Runnable callback) throws HtpError
    {
	String response;
	try {
	    response = waitResponse();
	}
	catch (IOException e) {
	    throw new HtpError("IOException waiting for response!");
	}

	System.out.println("controller: response: '" + response.trim() + "'");

	if (response.charAt(0) == '=') {
	    m_success = true;
	    m_response = response.substring(1);
	} else if (response.charAt(0) == '?') {
	    m_success = false;
	    m_response = response.substring(1);
	} else {
	    m_response = response;
	    m_success = false;
	    throw new HtpError("Invalid HTP response:\n" + response);
	}
    }

    private String waitResponse() throws IOException
    {
	StringBuilder ret = new StringBuilder();
	char buffer[] = new char[1024];
	while (true) {
	    //System.out.println("controller: blocking on response...");
	    int n = m_in.read(buffer, 0, 1024);
	    String str = new String(buffer, 0, n);
	    //System.out.println("controller: read '" + str + "', length = " + 
	    //		       str.length());
	    
	    if (n == -1){
		m_connected = false;
		break;
	    } else if (n > 0) {
		ret.append(cleanInput(new String(buffer,0,n)));
		String sofar = ret.toString();
		int len = sofar.length();
		//System.out.println("controller: sofar = '" + sofar + "'");
		if (len > 2 && 
		    sofar.charAt(len-1) == '\n' && 
		    sofar.charAt(len-2) == '\n') {
		    break;
		}
	    }
	}
	//System.out.println("controller: done waiting on response.");
	return ret.toString();
    }

    /** Cleans the input.  Removes all occurances of and '\r'. 
	Converts all '\t' to ' '. 
    */
    private String cleanInput(String in)
    {
	StringBuilder out = new StringBuilder();
	for (int i=0; i<in.length(); i++) {
	    if (in.charAt(i) == '\t') 
		out.append(' ');
	    else if (in.charAt(i) != '\r') {
		out.append(in.charAt(i));
	    }
	}      
	return out.toString();
    }


    private boolean m_connected;
    private BufferedReader m_in;
    private PrintStream m_out;   
 
    private String m_response;
    private boolean m_success;
}

//----------------------------------------------------------------------------
