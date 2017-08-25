def version(){
	Process p = "chromium-browser --version".execute()
	def data = p.text.replaceAll('.*Chromium ', '').replaceAll(' Built on.*', '').split('\n')
	data[data.length - 1]
}

def majorVersion(){
	version().split('\\.')[0]
}