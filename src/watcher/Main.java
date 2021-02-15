package watcher;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Logger;

import watcher.utility.VisitorUtility;


public class Main{

	private final static Logger LOGGER = Logger.getLogger("WATCHER");

	public static void main(String[] args) throws Exception {
		
		LOGGER.info("START WATCHER");
		WatchService watcher = FileSystems.getDefault().newWatchService();
		
		Files.walkFileTree(Paths.get("C:\\Users\\prima\\Desktop\\poste"), VisitorUtility.visitor(watcher));

		LOGGER.info("WATCHER IS READY");

		WatchKey key;
		String eventString = null;
		boolean isDir;
		
		while((key = watcher.take()) != null) {
			
			for(WatchEvent<?> event : key.pollEvents()) {

				String path = key.watchable() + File.separator + event.context();
				isDir = Files.isDirectory(Paths.get(path));
				WatchEvent.Kind<?> eventKind = event.kind();

				if(isDir) {

					eventString = String.format("Event: %s, directory affected: %s", eventKind.toString(), path);

					if(eventKind.toString().equals("ENTRY_CREATE")) {

						Paths.get(path).register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
								StandardWatchEventKinds.ENTRY_DELETE,
								StandardWatchEventKinds.ENTRY_MODIFY);
					}
				}else {

					eventString = String.format("Event: %s, file affected: %s", eventKind.toString(), path);
				}
			}
			key.reset();
			
			LOGGER.info("********** "+eventString+" **********");
		}
	}
	
//	private static FileVisitor<Path> visitor(WatchService watcher){
//		
//		return new FileVisitor<Path>() {
//
//			@Override
//			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
//				
//				dir.register(watcher, 
//						StandardWatchEventKinds.ENTRY_CREATE,
//						StandardWatchEventKinds.ENTRY_DELETE,
//						StandardWatchEventKinds.ENTRY_MODIFY);
//				
//				return FileVisitResult.CONTINUE;
//			}
//
//			@Override
//			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//				return FileVisitResult.CONTINUE;
//			}
//
//			@Override
//			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//				return FileVisitResult.CONTINUE;
//			}
//
//			@Override
//			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
//				return FileVisitResult.SKIP_SUBTREE;
//			}
//			
//		};
//	}
}
