package org.geekbang.projects.login.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class RepositoriesController {

	@Autowired
	private GitHub github;

	@Autowired
	private ConnectionRepository connectionRepository;

	@GetMapping("/repositories")
	public String repositories(Model model) {
		if (connectionRepository.findPrimaryConnection(GitHub.class) == null) {
			return "redirect:/connect/github";
		}

		String name = github.userOperations().getUserProfile().getName();
		String username = github.userOperations().getUserProfile()
				.getUsername();
		model.addAttribute("name", name);

		String uri = "https://api.github.com/users/{user}/repos";
		GitHubRepo[] repos = github.restOperations().getForObject(uri,
				GitHubRepo[].class, username);
		model.addAttribute("repositories", Arrays.asList(repos));

		return "repositories";
	}
	
	

}
