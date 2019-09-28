import com.nduginets.softwaredesign.LRUCache.LRUCache;
import com.nduginets.softwaredesign.LRUCache.LRUCacheImpl;

public class Main {

    public static void main(String[] args) {
        System.err.println("WW");
        LRUCache<Integer, String> cache = new LRUCacheImpl<>(5);
        cache.push(null, "1");
    }
}
