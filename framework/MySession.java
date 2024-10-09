package framework;

import javax.servlet.http.HttpSession;

public class MySession {
    private HttpSession session;

    public MySession() {
    }

    public MySession(HttpSession session) {
        this.session = session;
    }

    public void add(String key, Object value) {
        session.setAttribute(key, value);
    }

    public void delete(String key) {
        session.removeAttribute(key);
    }

    public Object get(String key) {
        return session.getAttribute(key);
    }
}