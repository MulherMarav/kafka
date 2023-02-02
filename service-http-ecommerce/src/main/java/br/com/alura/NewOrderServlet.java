package br.com.alura;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
    private final KafkaDispatcher<Email> emailDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
        emailDispatcher.close();
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        try {
            //we are not caring about any security issues, we are only
            //showing how to use http as a starting point
            var address = req.getParameter("email");
            var amount = new BigDecimal(req.getParameter("amount"));

            var subject = "Thank you!";
            var body = "Thank you for order! We are processing your order!";

            var email = new Email(subject, body, address);
            var orderId = UUID.randomUUID().toString();

            var order = new Order(orderId, amount, address);

            orderDispatcher.send("ECOMMERCE_NEW_ORDER", address, order);

            emailDispatcher.send("ECOMMERCE_SEND_EMAIL", address, email);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("New order sent successfully.");
        } catch (ExecutionException e) {
            throw new ServletException(e);
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }
    }

}
