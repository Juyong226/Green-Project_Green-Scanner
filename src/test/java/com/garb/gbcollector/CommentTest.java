package com.garb.gbcollector;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.garb.gbcollector.web.service.FeedCommentService;
import com.garb.gbcollector.web.vo.FeedCommentVO;

@SpringBootTest
class CommentTest {

	@Autowired
	private FeedCommentService commentService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test
	public void registerComments() {
		int number = 20;

		for (int i = 1; i <= number; i++) {
			FeedCommentVO params = new FeedCommentVO();
			params.setFeedNo((Integer) 61); // 댓글을 추가할 게시글 번호
			params.setContent(i + "번 댓글을 추가합니다!");
			params.setWriter(i + "번 회원");
			commentService.registerComment(params);
		}

		logger.debug("댓글 " + number + "개가 등록되었습니다.");
	}

	@Test
	public void deleteComment() {
		commentService.deleteComment((Integer) 10); // 삭제할 댓글 번호

		getCommentList();
	}

	@Test
	public void getCommentList() {
		FeedCommentVO params = new FeedCommentVO();
		params.setFeedNo((Integer) 61); // 댓글을 추가할 게시글 번호

		System.out.println(commentService.getCommentList(params));
	}

}
