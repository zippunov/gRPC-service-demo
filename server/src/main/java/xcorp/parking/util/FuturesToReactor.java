package xcorp.parking.util;

import com.datastax.oss.driver.api.core.AsyncPagingIterable;
import com.datastax.oss.driver.api.core.MappedAsyncPagingIterable;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

import static com.google.common.util.concurrent.Futures.addCallback;

public class FuturesToReactor {

    public static <T> Flux<T> pagingToFlux(CompletionStage<MappedAsyncPagingIterable<T>> promise) {
        // not correct - missing next pages, will be error if result set is > 5000 records (which is wrong anyway)
        return Mono.fromCompletionStage(promise)
                .map(AsyncPagingIterable::currentPage)
                .flatMapMany(Flux::fromIterable);
    }

    public static <T> Mono<T> listenableFuture2Mono(ListenableFuture<T> f) {
        return Mono.create(sink -> {
            addCallback(
                    f,
                    new FutureCallback<T>() {
                        @Override
                        public void onSuccess(T result) {
                            sink.success(result);
                        }

                        @Override
                        public void onFailure(Throwable err) {
                            sink.error(err);
                        }
                    },
                    MoreExecutors.directExecutor()
            );
        });
    }

}
