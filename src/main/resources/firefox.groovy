def version(){
	Process p = "firefox --version".execute()
	p.text.minus('Mozilla Firefox ')
}

def majorVersion(){
	version().split('\\.')[0]
}