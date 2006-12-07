//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.htp;

import java.io.InputStream;
import java.io.OutputStream;

//----------------------------------------------------------------------------

/** Takes input from the input stream and sends it to the output stream. */
public class StreamCopy
    implements Runnable
{ 
    public StreamCopy(InputStream in, OutputStream out)
    {
	m_in = in;
	m_out = out;
    }

    public void run()
    {
	System.out.println(this.toString() + ": run()");

	try {
	    byte buffer[] = new byte[1024];
	    while (true) {
		System.out.println(this.toString() + ": blocking...");
		int n = m_in.read(buffer);
		if (n < 0) {
		    m_out.close();
		    break;
		} else if (n == 0) {
		    Thread.sleep(100);
		} else if (n > 0) {
		    m_out.write(buffer, 0, n);
		    m_out.flush();
		    String str = new String(buffer, 0, n);
		    System.out.println(this.toString() + " read/wrote: '" 
				       + str.trim() + "'");
		}
	    }
	} 
	catch (Throwable e) {
	    System.out.println(e.getMessage());
	}
    }

    InputStream m_in;
    OutputStream m_out;
}

//----------------------------------------------------------------------------
