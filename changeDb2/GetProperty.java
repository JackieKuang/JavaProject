package changeDb2;

import java.sql.*;
import java.util.*;
import java.io.*;

public class GetProperty {
    Properties pt;
    
    GetProperty() {}

    Properties setProperty() {
            try{
                    pt = new Properties();
                    pt.load(new FileInputStream("setup.conf"));
            }
            catch(IOException e) {}
            return pt;
    }
}
