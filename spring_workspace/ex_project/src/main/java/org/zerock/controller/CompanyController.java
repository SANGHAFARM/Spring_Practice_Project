package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.CompanyVO;
import org.zerock.domain.Criteria;
import org.zerock.service.CompanyService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/company/*")
@AllArgsConstructor // CompanyService 자동 주입
public class CompanyController {

	private CompanyService service;

	// 1. 거래처 목록 및 검색 조회
	@GetMapping("/list")
	public void list(@ModelAttribute("cri") Criteria cri, Model model) {
		log.info("company list cri: " + cri);
		model.addAttribute("list", service.getList(cri));
	}

	// 2. 거래처 등록 화면 이동
	@GetMapping("/register")
	public void register() {
		// views/company/register.jsp로 자동 이동
	}

	// 3. 거래처 등록 처리
	@PostMapping("/register")
	public String register(CompanyVO company, RedirectAttributes rttr) {
		log.info("register: " + company);

		service.register(company);

		// 등록 직후 모달창이나 알림 메시지에 띄울 거래처 코드를 일회성으로 보관
		rttr.addFlashAttribute("result", company.getCompanyCode());

		return "redirect:/company/list";
	}

	// 4. 조회/수정 화면 이동 (companyName으로 조회)
	@GetMapping({ "/get", "/modify" })
	public void get(@RequestParam("companyName") String companyName, @ModelAttribute("cri") Criteria cri, Model model) {
		log.info("/get or /modify companyName: " + companyName);
		model.addAttribute("company", service.get(companyName));
	}

	// 5. 상호 기준 수정 처리
	@PostMapping("/modify")
	public String modify(CompanyVO company, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		log.info("modify: " + company);

		if (service.modify(company)) {
			rttr.addFlashAttribute("result", "success");
		}

		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());

		return "redirect:/company/list";
	}

	// 6. 상호 기준 삭제 처리
	@PostMapping("/remove")
	public String remove(@RequestParam("companyName") String companyName, @ModelAttribute("cri") Criteria cri,
			RedirectAttributes rttr) {
		log.info("remove by companyName: " + companyName);

		if (service.remove(companyName)) {
			rttr.addFlashAttribute("result", "success");
		}

		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());

		return "redirect:/company/list";
	}
}