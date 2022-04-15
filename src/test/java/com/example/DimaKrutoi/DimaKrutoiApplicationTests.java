package com.example.DimaKrutoi;

import com.example.DimaKrutoi.entity.Tree;
import com.example.DimaKrutoi.repository.TreeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DimaKrutoiApplicationTests {

	@Autowired
	private TreeRepository treeRepository;

	@Test
	void contextLoads() {

		List<Tree> treesBeforeSave = treeRepository.findAll();

		int treesWithTestIdBeforeSave = treesBeforeSave.stream().filter(tree -> "testId".equals(tree.getId())).collect(Collectors.toList()).size();

		Tree testTree = new Tree();
		testTree.setHeight(7)
				.setName("testTree")
				.setId("testId")
				.setCreationTS(LocalDateTime.now());

		treeRepository.save(testTree);

		List<Tree> treesAfterSave = treeRepository.findAll();

		int treesWithTestIdAfterSave = treesAfterSave.stream().filter(tree -> "testId".equals(tree.getId())).collect(Collectors.toList()).size();

		assertEquals(treesWithTestIdBeforeSave + 1 , treesWithTestIdAfterSave );

		System.out.println("Test's workin");
	}

}
