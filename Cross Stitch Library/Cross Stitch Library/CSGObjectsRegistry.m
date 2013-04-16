#import "CSGObjectsRegistry.h"

#import "CSGThread.h"
#import "CSGObjectsSetInMemory.h"



@interface CSGObjectsRegistry ()
{
}

@property (nonatomic, retain) CSGThread *tmpThread;
@property (nonatomic, retain) CSGObjectsSetInMemory *threadsSet;

@end

@implementation CSGObjectsRegistry

@synthesize tmpThread;
@synthesize threadsSet;

- (id) init
{
    if (self = [super init])
    {
        tmpThread = [CSGThread alloc];
        threadsSet = [[CSGObjectsSetInMemory alloc] init];
    }
    return self;
}


- (CSGThread*) getThreadWithColor: (UIColor*) aColor
{
    tmpThread = [tmpThread initWithColor:aColor];
    CSGThread* aThread = [threadsSet member:tmpThread];
    if (!aThread)
    {
        aThread = [[CSGThread alloc] initWithColor:aColor];
        [threadsSet putObject:aThread];
    }
    return aThread;
}

- (CSGThread*) getThread: (CSGThread*) anInstance
{
    return anInstance;
}

- (CSGThreadInBlend*) getThreadInBlend: (CSGThreadInBlend*) anInstance
{
    return anInstance;
}

- (CSGThreadsBlend*) getThreadsBlend: (CSGThreadsBlend*) anInstance
{
    return anInstance;
}

- (CSGStitchInCell*) getStitchInCell: (CSGStitchInCell*) anInstance
{
    return anInstance;
}

- (CSGDesignCell*) getDesignCell: (CSGDesignCell*) anInstance
{
    return anInstance;
}

- (CSGDesignPoint*) getDesignPoint: (CSGDesignPoint*) anInstance
{
    return anInstance;
}

- (CSGDesignPoints*) getDesignPoints: (CSGDesignPoints*) anInstance
{
    return anInstance;
}

- (CSGBackStitch*) getBackStitch: (CSGBackStitch*) anInstance
{
    return anInstance;
}

- (CSGStraightStitch*) getStraightStitch: (CSGStraightStitch*) anInstance
{
    return anInstance;
}

- (CSGDesign*) getDesign: (CSGDesign*) anInstance
{
    return anInstance;
}


@end