package estore.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import estore.repository.Order;
import estore.repository.Status;
import estore.repository.service.OrderService;
import estore.repository.service.StatusService;

@Controller
public class OrderAController {
	@Autowired
	OrderService orderService;
	
	@Autowired
	StatusService statusService;
	
	@RequestMapping("/admin/order/reset")
	public String reset() {
		return "forward:/admin/order/index";
	}
	
	@RequestMapping("/admin/order/index")
	public String index(Model model) {
		model.addAttribute("item", new Order());
		
		return this.forward(model);
	}
	
	@RequestMapping("/admin/order/edit/{id}")
	public String edit(Model model, @PathVariable("id") Long id) {
		Order item = orderService.getById(id);
		model.addAttribute("item", item);
		
		return this.forward(model);
	}
	
	@RequestMapping("/admin/order/create")
	public String create(Model model, @ModelAttribute("item") Order item) {
		orderService.create(item);
		model.addAttribute("message", "Create successfully!");
		
		return this.forward(model);
	}
	
	@RequestMapping("/admin/order/update")
	public String update(Model model, @ModelAttribute("item") Order item) {
		orderService.update(item);
		model.addAttribute("message", "Update successfully!");
		
		return this.forward(model);
	}
	
	@RequestMapping("/admin/order/delete/{id}")
	public String delete(Model model, @PathVariable("id") Long id) {
		orderService.deleteById(id);
		model.addAttribute("message", "Delete successfully!");
		
		model.addAttribute("item", new Order());
		return this.forward(model);
	}
	
	String forward(Model model) {
		List<Order> items = orderService.findAll();
		model.addAttribute("items", items);
		return "admin/order/index";
	}
	
	@ModelAttribute("statuses")
	public List<Status> getStatusList(){
		return statusService.findAll();
	}
}