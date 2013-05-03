#import "CSGDesign.h"

@implementation CSGDesign

@synthesize width;
@synthesize height;
@synthesize cells;
@synthesize backStitches;
@synthesize straightStitches;

- (id) init
{
    NSAssert( false, @"Please use designated initializer" );
    
    return nil;
}

-(id) initWithWidth: (uint32_t) aWidth Height: (uint32_t) aHeight Cells: (NSArray*) aCells BackStitches: (NSArray*) aBackStitches StraightStitches: (NSArray*) aStraightStitches
{
    if (self = [super init])
    {
        width = aWidth;
        height = aHeight;
        cells = aCells;
        backStitches = aBackStitches;
        straightStitches = aStraightStitches;
    }
    return self;
}


- (BOOL) isEqual: (id) object
{
    if (object == self)
    {
        return YES;
    }
    if (!object || ![object isKindOfClass:[self class]])
    {
        return NO;
    }
    
    return [self isEqualToCSGDesign: object];
}

- (BOOL) isEqualToCSGDesign: (CSGDesign*) aDesign
{
    if (self == aDesign)
    {
        return YES;
    }
    if (width != aDesign.width)
    {
        return NO;
    }
    if (height != aDesign.height)
    {
        return NO;
    }
    for(uint32_t i = 0; i < width * height; ++i)
    {
        if (![[cells objectAtIndex:i] isEqual:[aDesign.cells objectAtIndex:i]])
        {
            return NO;
        }
    }
    
    if (backStitches.count != aDesign.backStitches.count)
    {
        return NO;
    }
    
    for(uint32_t i = 0; i < backStitches.count; ++i)
    {
        if (![[backStitches objectAtIndex:i] isEqual:[aDesign.backStitches objectAtIndex:i]])
        {
            return NO;
        }
    }
    
    if (straightStitches.count != aDesign.straightStitches.count)
    {
        return NO;
    }
    
    for(uint32_t i = 0; i < straightStitches.count; ++i)
    {
        if (![[straightStitches objectAtIndex:i] isEqual:[aDesign.straightStitches objectAtIndex:i]])
        {
            return NO;
        }
    }
    
    return YES;
}

- (NSUInteger)hash
{
    NSUInteger hash = 17;
    hash = hash*31 + width;
    hash = hash*31 + height;
    for(uint32_t i = 0; i < width * height; ++i)
    {
        CSGDesignCell* o = [cells objectAtIndex:i];
        hash = hash*31 + o.hash;
    }
    for(uint32_t i = 0; i < backStitches.count; ++i)
    {
        CSGBackStitch* o = [backStitches objectAtIndex:i];
        hash = hash*31 + o.hash;
    }
    for(uint32_t i = 0; i < straightStitches.count; ++i)
    {
        CSGStraightStitch* o = [straightStitches objectAtIndex:i];
        hash = hash*31 + o.hash;
    }
    
    return hash;
}


@end