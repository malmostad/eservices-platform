<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>

<div class="main">
	<div class="columns-3">
		<div class="content-wrapper">
			<div class="content-wrapper-1">
				<div class="content-wrapper-2">
					<div class="content-wrapper-3">
						<div class="content-wrapper-4">
							<div class="content-wrapper-5">
								<div class="content-wrapper-6">
									<hst:include ref="leftmenu" />
									<div class="content">
										<hst:include ref="content" />
										<!-- the lists is a general 'slot' where items can be dropped in -->
										<hst:include ref="lists" />
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="aside">
			<hst:include ref="right" />
		</div>
	</div>
</div>
